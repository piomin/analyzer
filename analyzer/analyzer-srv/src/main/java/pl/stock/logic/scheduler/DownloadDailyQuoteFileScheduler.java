package pl.stock.logic.scheduler;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pl.stock.commons.file.DownloadFileUtils;
import pl.stock.commons.parser.DailyQuotesFileParser;
import pl.stock.commons.parser.MultiQuotesFileParser;
import pl.stock.data.entity.DailyQuoteRecord;
import pl.stock.logic.enums.UpdateType;
import pl.stock.logic.service.StockLogicService;

/**
 * Class for download and process daily quotes file
 * @author Piotr Mińkowski
 *
 */
@Service
@PropertySource("classpath:pl/stock/logic/stock-logic.properties")
public class DownloadDailyQuoteFileScheduler {

	@Value("${scheduler.quotes.active}")
	private boolean active;
	
	// URL of file with daily quotes
	@Value("${daily_quote.url}")
	private String stocksURL;
	@Value("${funds_daily.url}")
	private String fundsURL;

	// URL of file with all quotes from beginning
	@Value("${all_quotes.url}")
	private String allStocksURL;
	@Value("${funds_all.url}")
	private String allFundsURL;
	
	// blacklist for companies imported from file
	@Value("${blacklist.pattern}")
	private String blacklist;
	private Pattern blacklistPattern;

	// parsers for input files
	@Autowired
	private MultiQuotesFileParser multiParser;
	@Autowired
	private DailyQuotesFileParser dailyParser;

	// main stock logic processor 
	@Autowired
	private StockLogicService stockLogic;

	@Autowired
	private ApplicationContext context;

	private final Logger LOGGER = Logger.getLogger(DownloadDailyQuoteFileScheduler.class);
	private final pl.stock.data.beans.UpdateType[] updates = { pl.stock.data.beans.UpdateType.STOCK };

	/**
	 * Schedule download daily quote file task
	 */
	@Scheduled(cron = "*/60 * * * * ?")
	public void schedule() {

		// do not run scheduler if is set to inactive
		if (!active) {
			return;
		}
		
		// set blacklist prefixes table
		blacklistPattern = Pattern.compile(blacklist);
		
		for (pl.stock.data.beans.UpdateType update : updates) {
			try {
				final UpdateType updateType = stockLogic.detectUpdateType(update);
				LOGGER.info(MessageFormat.format("Updating {0}", updateType));

				// add stock index for funds if not exists
				if (updateType == UpdateType.INITIAL && update == pl.stock.data.beans.UpdateType.FUNDS) {
					stockLogic.createIndexIfNotExists("FUNDUSZE");
				}
				
				switch (updateType) {
				case INITIAL:
					processInitial(update);
					break;

				case DAILY:
					processDaily(update);
					break;

				case MULTI:
					processMulti(update);
					break;

				default:
					break;
				}

			} catch (IOException e) {
				LOGGER.error("EXCEPTION.DOWNLOAD.FILE", e);
			} catch (ParseException e) {
				LOGGER.error("EXCEPTION.PARSE.FILE", e);
			}
		}
	}

	/**
	 * Process scheduled task
	 * @throws ParseException - exception while parsing file 
	 * @throws IOException - exception while opening file
	 */
	private void processDaily(pl.stock.data.beans.UpdateType updateType) throws IOException, ParseException {

		// download file with quotes from URL
		String fileUrl = null;
		if (updateType == pl.stock.data.beans.UpdateType.STOCK) {
			fileUrl = stocksURL;
		} else {
			fileUrl = fundsURL;
		}
		final File temp = DownloadFileUtils.downloadAndSaveURL(fileUrl);
		
		// translate file into daily record object list
		final List<DailyQuoteRecord> records = (List<DailyQuoteRecord>) dailyParser.parse(temp, blacklistPattern);

		// check if quotes are actual or should be updated
		if (records.size() > 0) {
			DailyQuoteRecord firstRecord = records.get(0);
			LOGGER.info(MessageFormat.format("{0}|Quotes date", new DateFormatter("yyyy-MM-dd HH:mm").print(firstRecord.getDate(), new Locale("pl"))));
			if (stockLogic.isDataActual(updateType, firstRecord.getDate())) {
				LOGGER.info(MessageFormat.format("{0}|Data is actual", updateType));
				return;
			} else {
				LOGGER.info(MessageFormat.format("{0}|Data is not actual - processing", updateType));
			}
		} else {
			return;
		}
		
		// call main processor
		stockLogic.processQuoteUpdate(records, updateType, true);

		// process statistic calculation for parsed quotes
		for (DailyQuoteRecord quote : records) {
			stockLogic.processDailyCalculation(quote.getCompany().getSymbol(), new Date());
		}
	}

	/**
	 * Process scheduled task
	 * @throws ParseException - exception while parsing file 
	 * @throws IOException - exception while opening file
	 */
	private void processInitial(pl.stock.data.beans.UpdateType updateType) throws IOException, ParseException {

		// download file with quotes from URL
		String fileUrl = null;
		if (updateType == pl.stock.data.beans.UpdateType.STOCK) {
			fileUrl = allStocksURL;
		} else {
			fileUrl = allFundsURL;
		}
		final File temp = DownloadFileUtils.downloadAndSaveURL(fileUrl);

		// translate file into daily record object list
		final Map<String, List<DailyQuoteRecord>> records = (Map<String, List<DailyQuoteRecord>>) multiParser.parse(temp, blacklistPattern);

		// process in parallel records for all companies
		for (String companyKey : records.keySet()) {			
			try {
				final String key = companyKey;
				stockLogic.processInitialQuoteUpdate(records.get(key), updateType);
				stockLogic.processInitialCalculation(key);
			} catch (Exception e) {
				LOGGER.error("EXCEPTION.IMPORT." + companyKey, e);
			}
		}

		// store update 
		stockLogic.saveUpdate(updateType, multiParser.getLatestDate());
	}

	/**
	 * Process scheduled task
	 * @throws IOException
	 * @throws ParseException
	 */
	private void processMulti(pl.stock.data.beans.UpdateType updateType) throws IOException, ParseException {

		// download file with quotes from URL
		String fileUrl = null;
		if (updateType == pl.stock.data.beans.UpdateType.STOCK) {
			fileUrl = allStocksURL;
		} else {
			fileUrl = allFundsURL;
		}
		final File temp = DownloadFileUtils.downloadAndSaveURL(fileUrl);
		Date lastModified = new Date(temp.lastModified());

		// get last update
		final Date update = stockLogic.getLastUpDate();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(update);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		
		// process only if data file date modification older than last update 
		LOGGER.info(MessageFormat.format("MULTI|Data file last update:{0}", new DateFormatter("yyyy-MM-dd HH:mm").print(lastModified, new Locale("pl"))));
		if (!lastModified.after(update)) {
			LOGGER.info("MULTI|Data actual");
			return;
		}
		
		// translate file into daily record object list
		final Map<String, List<DailyQuoteRecord>> records = (Map<String, List<DailyQuoteRecord>>) multiParser.parse(temp, blacklistPattern);

		// process in parallel records for all companies
		for (String companyKey : records.keySet()) {
			try {
				// get quotes for company
				final List<DailyQuoteRecord> quotes = records.get(companyKey);

				// create new list with quotes filtered by update date
				final List<DailyQuoteRecord> quotesToAdd = new ArrayList<DailyQuoteRecord>();
				for (DailyQuoteRecord quote : quotes) {
					if (update.before(quote.getDate())) {
						quotesToAdd.add(quote);
					}
				}
				LOGGER.info(MessageFormat.format("{0}|Processing quotes:{1}", companyKey, quotesToAdd.size()));
				
				// store quotes and calculate statistic for them
				stockLogic.processQuoteUpdate(quotesToAdd, updateType, false);
				for (DailyQuoteRecord quote : quotesToAdd) {
					stockLogic.processDailyCalculation(companyKey, quote.getDate());
				}
			} catch (Exception e) {
				LOGGER.error("EXCEPTION.IMPORT." + companyKey, e);
			}
		}

		// store update 
		stockLogic.saveUpdate(updateType, multiParser.getLatestDate());
	}
}
