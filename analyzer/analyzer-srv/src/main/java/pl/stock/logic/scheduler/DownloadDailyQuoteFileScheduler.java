package pl.stock.logic.scheduler;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pl.stock.commons.file.DownloadFileUtils;
import pl.stock.commons.parser.DailyQuotesFileParser;
import pl.stock.commons.parser.MultiQuotesFileParser;
import pl.stock.data.entity.DailyQuoteRecord;
import pl.stock.logic.service.StockLogicService;

/**
 * Class for download and process daily quotes file
 * @author Piotr Mińkowski
 *
 */
@Service
public class DownloadDailyQuoteFileScheduler {

	// URL of file with daily quotes
	@Value("${daily_quote.url:http://bossa.pl/pub/ciagle/mstock/sesjaall/sesjacgl.prn}")
	private String fileURL;

	// URL of file with all quotes from beginning
	@Value("${all_quotes.url:http://bossa.pl/pub/ciagle/mstock/mstcgl.zip}")
	//	@Value("${all_quotes.url:file:///C:/Users/piomin/Workspaces/stockgpw/stock-gpw/stock-logic/src/test/resources/pl/stock/logic/KGHM.zip}")
	private String initialFileURL;

	// parser for quotes history
	@Autowired
	private MultiQuotesFileParser multiParser;

	// daily file records parser
	@Autowired
	private DailyQuotesFileParser dailyParser;

	// main stock logic processor 
	@Autowired
	private StockLogicService stockLogic;

	@Autowired
	private ApplicationContext context;

	// log object
	private final Logger LOGGER = Logger.getLogger(DownloadDailyQuoteFileScheduler.class);

	/**
	 * Schedule download daily quote file task
	 */
	@Scheduled(cron = "*/60 * * * * ?")
	public void schedule() {

		try {
			// check if it's initial update
			if (stockLogic.checkIfInitialUpdate()) {
				LOGGER.info("Initial update");

				// process all available quotes (large update)
				processInitial();
			} else {
				if (stockLogic.checkIfUpdatePerformed()) {
					LOGGER.info("Daily update");

					// process daily quotes
					processDaily();

					// process daily statistic calculation
					stockLogic.processDailyCalculation();
				}
			}
		} catch (IOException e) {
			LOGGER.error("EXCEPTION.DOWNLOAD.FILE", e);
		} catch (ParseException e) {
			LOGGER.error("EXCEPTION.PARSE.FILE", e);
		}
	}

	/**
	 * Process scheduled task
	 * @throws ParseException - exception while parsing file 
	 * @throws IOException - exception while opening file
	 */
	private void processDaily() throws IOException, ParseException {

		// download file with quotes from URL
		final File temp = DownloadFileUtils.downloadAndSaveURL(fileURL);

		// translate file into daily record object list
		final List<DailyQuoteRecord> records = (List<DailyQuoteRecord>) dailyParser.parse(temp);

		// call main processor
		stockLogic.processQuoteUpdate(records, true);
	}

	/**
	 * Process scheduled task
	 * @throws ParseException - exception while parsing file 
	 * @throws IOException - exception while opening file
	 */
	private void processInitial() throws IOException, ParseException {

		// download file with quotes from URL
		final File temp = DownloadFileUtils.downloadAndSaveURL(initialFileURL);

		// translate file into daily record object list
		final Map<String, List<DailyQuoteRecord>> records = (Map<String, List<DailyQuoteRecord>>) multiParser.parse(temp);

		// process in parallel records for all companies
		for (String companyKey : records.keySet()) {
			final String key = companyKey;
			stockLogic.processQuoteUpdate(records.get(key), false);
			stockLogic.processCalculationForCompany(key);
		}

		// store update 
		stockLogic.saveUpdate();
	}

}
