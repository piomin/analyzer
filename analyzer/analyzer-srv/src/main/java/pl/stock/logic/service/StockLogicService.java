package pl.stock.logic.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import pl.stock.data.beans.UpdateStatus;
import pl.stock.data.dto.StatisticDetails;
import pl.stock.data.dto.StatisticRecordSimple;
import pl.stock.data.entity.Company;
import pl.stock.data.entity.DailyQuoteRecord;
import pl.stock.data.entity.StatisticRecord;
import pl.stock.data.entity.StockIndex;
import pl.stock.data.entity.UpdateHistory;
import pl.stock.data.service.CompanyService;
import pl.stock.data.service.DailyQuoteRecordService;
import pl.stock.data.service.StatisticRecordService;
import pl.stock.data.service.StockIndexService;
import pl.stock.data.service.UpdateHistoryService;
import pl.stock.logic.enums.UpdateType;

@Service
@Transactional(readOnly = false)
@TransactionConfiguration(defaultRollback = false)
public class StockLogicService {
	private static final Logger LOGGER = Logger.getLogger(StockLogicService.class);

	@Autowired
	private CompanyService companyService;
	@Autowired
	private StockIndexService indexService;
	@Autowired
	private DailyQuoteRecordService quoteService;
	@Autowired
	private StatisticRecordService statisticService;
	@Autowired
	private UpdateHistoryService updateService;
	@Autowired
	private CalculationService calculationService;
	@Autowired
	private ConversionService conversionService;

	private int tasksInProgress = 0;

	/**
	 * Process daily file update schedule
	 * @param records - list of records read from file
	 */
	public void processInitialQuoteUpdate(final List<DailyQuoteRecord> records, final pl.stock.data.beans.UpdateType type, boolean finished) {

		// number of records added
		tasksInProgress++;

		// finish if there is no records to process
		if (records.size() == 0) {
			return;
		}
		
		// set company get from database in DailyQuoteRecord
		final String symbol = records.get(0).getCompany().getSymbol();
		Company company = companyService.findBySymbol(symbol);
		if (company == null) {
			company = new Company(symbol, symbol);
			Integer id = companyService.add(company);
			company.setId(id);
			LOGGER.info(MessageFormat.format("{0}|Company added:{1}", symbol, company.getId()));
			if (type == pl.stock.data.beans.UpdateType.FUNDS) {
				StockIndex index = indexService.findByName("FUNDUSZE");
				index.getCompanies().add(company);
			}
		}
		
		// iterating over all daily read records
		for (DailyQuoteRecord record : records) {

			// set company get from database in DailyQuoteRecord
			record.setCompany(company);

			// save daily quote record in database
			final Long recordId = quoteService.add(record);
			LOGGER.debug(MessageFormat.format("{0}|Daily quote added:{1}", symbol, recordId));
		}
		LOGGER.info(MessageFormat.format("{0}|Daily quotes stored", symbol));
		
		// save information about update status in database if all data imported
		if (finished) {
			saveUpdate(type);
		} else {
			tasksInProgress--;
		}

	}
	
	/**
	 * Process daily file update schedule
	 * @param records - list of records read from file
	 */
	public void processQuoteUpdate(final List<DailyQuoteRecord> records, final pl.stock.data.beans.UpdateType type, boolean finished) {

		// number of records added
		tasksInProgress++;

		// iterating over all daily read records
		for (DailyQuoteRecord record : records) {

			// set company get from database in DailyQuoteRecord
			final String symbol = record.getCompany().getSymbol();
			Company company = companyService.findBySymbol(symbol);
			if (company == null) {
				company = new Company(symbol, symbol);
				Integer id = companyService.add(company);
				company.setId(id);
				LOGGER.info(MessageFormat.format("{0}|Company added:{1}", symbol, company.getId()));
				if (type == pl.stock.data.beans.UpdateType.FUNDS) {
					StockIndex index = indexService.findByName("FUNDUSZE");
					index.getCompanies().add(company);
				}
			}
			record.setCompany(company);

			// save daily quote record in database
			final Long recordId = quoteService.add(record);
			LOGGER.info(MessageFormat.format("{0}|Daily quote added:{1}", symbol, recordId));
		}

		// save information about update status in database if all data imported
		if (finished) {
			saveUpdate(type);
		} else {
			tasksInProgress--;
		}

	}

	/**
	 * Detect update type. Possible values are NONE, TICKLY, DAILY, MULTI, INITIAL
	 * @return - update type
	 */
	public UpdateType detectUpdateType(pl.stock.data.beans.UpdateType type) {
		if (updateService.countByType(type) == 0) {
			return UpdateType.INITIAL;
		} else {
			// set today calendar
			final Calendar today = GregorianCalendar.getInstance();
			today.setTime(new Date());

			// on Saturday and Sunday update is not performed	
			final int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
				return UpdateType.NONE;
			}

			// find update and set update date calendar
			final UpdateHistory updateHistory = updateService.findNewestByType(type);
			final Calendar update = GregorianCalendar.getInstance();
			update.setTime(updateHistory.getAddDate());
			final int todayDayOfYear = today.get(Calendar.DAY_OF_YEAR);
			final int updateDayOfYear = update.get(Calendar.DAY_OF_YEAR);
			if (todayDayOfYear == updateDayOfYear) {
				return UpdateType.TICKLY;
			} else if (updateDayOfYear == (todayDayOfYear - 1) || (updateDayOfYear == (todayDayOfYear - 3) && dayOfWeek == Calendar.MONDAY)) {
				return UpdateType.DAILY;
			} else {
				return UpdateType.MULTI;
			}
		}
	}

	public int getTasksInProgress() {
		return tasksInProgress;
	}

	/**
	 * Process daily statistic calculation for company
	 * @param companySymbol
	 * @param date
	 */
	public void processDailyCalculation(String companySymbol, Date date) {
		// find company by symbol
		final Company company = companyService.findBySymbol(companySymbol);

		// find daily quote history and last statistic record from database
		final List<DailyQuoteRecord> quotes = quoteService.findByCompanyOlderThan(company, date, 100);
		if (quotes.size() < 2) {
			LOGGER.warn(MessageFormat.format("{0}|No qoutes found", companySymbol));
			return;
		}
		final DailyQuoteRecord lastQuote = quotes.get(0);

		// count new statistic only if it does not exist
		if (lastQuote.getStatistic() == null) {
			final StatisticRecord previousStatistic = quotes.get(1).getStatistic();

			// initialize table with daily quote values history
			final int prizesLength = quotes.size();
			final double[] closes = new double[prizesLength];
			final double[] mins = new double[prizesLength];
			final double[] maxs = new double[prizesLength];
			final double[] volumens = new double[prizesLength];
			for (int i = 0; i < prizesLength; i++) {
				final DailyQuoteRecord quote = quotes.get(i);
				closes[i] = quote.getClose();
				mins[i] = quote.getMin();
				maxs[i] = quote.getMax();
				volumens[i] = quote.getVolumen();
			}

			// creating statistic record
			final StatisticRecord actualStatistic = new StatisticRecord();
			actualStatistic.setQuote(lastQuote);
			actualStatistic.setCompany(company);
			actualStatistic.setAddDate(date);

			// set number of quotes found in database
			calculationService.setQuoteCount(prizesLength);

			// calculate all EMA values for different periods
			actualStatistic.setEma5(calculationService.countSingleEma(5, lastQuote.getClose(), previousStatistic.getEma5()));
			actualStatistic.setEma10(calculationService.countSingleEma(10, lastQuote.getClose(), previousStatistic.getEma10()));
			actualStatistic.setEma20(calculationService.countSingleEma(20, lastQuote.getClose(), previousStatistic.getEma20()));
			actualStatistic.setEma50(calculationService.countSingleEma(50, lastQuote.getClose(), previousStatistic.getEma50()));
			actualStatistic.setEma100(calculationService.countSingleEma(100, lastQuote.getClose(), previousStatistic.getEma100()));
			actualStatistic.setEma12(calculationService.countSingleEma(12, lastQuote.getClose(), previousStatistic.getEma12()));
			actualStatistic.setEma14(calculationService.countSingleEma(14, lastQuote.getClose(), previousStatistic.getEma14()));
			actualStatistic.setEma26(calculationService.countSingleEma(26, lastQuote.getClose(), previousStatistic.getEma26()));

			// calculate all SMA values for different periods
			actualStatistic.setSma14(calculationService.countSma(14, 0, closes));
			actualStatistic.setSma28(calculationService.countSma(28, 0, closes));
			actualStatistic.setSma42(calculationService.countSma(42, 0, closes));

			// calculate RSI for standard period (14 days)
			actualStatistic.setRsi(calculationService.countRsi(closes));

			// calculate STS for standard period (9, 5 days)
			final double[] sts = calculationService.countSingleSts(closes, mins, maxs, previousStatistic.getStsEma());
			actualStatistic.setSts(sts[0]);
			actualStatistic.setStsEma(sts[1]);

			// calculate ATR for standard period
			actualStatistic.setAtr(calculationService.countSingleAtr(closes[1], lastQuote.getMin(), lastQuote.getMax(), previousStatistic.getAtr()));

			// calculate MACD for standard period 12, 26 and 14
			final double[] macd = calculationService.countSingleMacd(lastQuote.getClose(), previousStatistic.getEma12(), previousStatistic.getEma26(),
					previousStatistic.getMacdEma());
			actualStatistic.setMacd(macd[0]);
			actualStatistic.setMacdEma(macd[1]);

			// calculate RoC for standard period
			actualStatistic.setRoc(calculationService.countSingleRoc(closes));

			// calculate ADX for standard period (14 days)
			final double[] adx = calculationService.countAdx(closes, mins, maxs);
			actualStatistic.setDmiPlus(adx[0]);
			actualStatistic.setDmiMinus(adx[1]);
			actualStatistic.setAdx(adx[2]);

			// adding statistic to database
			final Long statisticId = statisticService.add(actualStatistic);
			LOGGER.info(MessageFormat.format("{0}|Statistic added:{1}", company.getSymbol(), statisticId));
		}
	}

	/**
	 * Calculate and store statistics from the beginning to current date for company
	 * @param companySymbol
	 */
	public void processInitialCalculation(String companySymbol) {
		Company company = companyService.findBySymbol(companySymbol);
		try {
			// find daily quote history and last statistic record from database
			List<DailyQuoteRecord> quotes = quoteService.findAllByCompany(company);

			// initialize table with daily quote values history
			final int prizesLength = quotes.size();
			final double[] closes = new double[prizesLength];
			final double[] mins = new double[prizesLength];
			final double[] maxs = new double[prizesLength];
			final double[] volumens = new double[prizesLength];
			for (int i = 0; i < prizesLength; i++) {
				final DailyQuoteRecord quote = quotes.get(i);
				closes[i] = quote.getClose();
				mins[i] = quote.getMin();
				maxs[i] = quote.getMax();
				volumens[i] = quote.getVolumen();
			}

			// calculate all EMA values for different periods
			final double[] emas5 = calculationService.countMultiEma(5, closes);
			final double[] emas10 = calculationService.countMultiEma(10, closes);
			final double[] emas12 = calculationService.countMultiEma(12, closes);
			final double[] emas14 = calculationService.countMultiEma(14, closes);
			final double[] emas20 = calculationService.countMultiEma(20, closes);
			final double[] emas26 = calculationService.countMultiEma(26, closes);
			final double[] emas50 = calculationService.countMultiEma(50, closes);
			final double[] emas100 = calculationService.countMultiEma(100, closes);

			// count EMA values for volumens
			final double[] avVol5 = calculationService.countMultiEma(5, volumens);
			final double[] avVol12 = calculationService.countMultiEma(12, volumens);
			final double[] avVol26 = calculationService.countMultiEma(26, volumens);
			final double[] avVol50 = calculationService.countMultiEma(50, volumens);

			// count SMA values
			double[] smas14 = calculationService.countMultiSma(14, closes);
			double[] smas28 = calculationService.countMultiSma(28, closes);
			double[] smas42 = calculationService.countMultiSma(42, closes);

			// calculate MACD for standard period 12, 26 and 14
			final double[][] macdRet = calculationService.countMultiMacd(closes);
			final double[] macds = macdRet[0];
			final double[] macdsEma = macdRet[1];

			// calculate RoC for standard period
			final double[][] rocRet = calculationService.countMultiRoc(closes);
			final double[] rocs = rocRet[0];
			final double[] rocsEma = rocRet[1];

			// calculate RSI
			final double[] rsis = calculationService.countMultiRsi(closes);

			// calculate ATR
			final double[] atrs = calculationService.countMultiAtr(closes, mins, maxs);

			// calculate ADX
			final double[][] adxRet = calculationService.countMultiAdx(closes, mins, maxs);
			final double[] dmiPs = adxRet[0];
			final double[] dmiMs = adxRet[1];
			final double[] adxs = adxRet[2];

			// calculate STS
			final double[][] stsRet = calculationService.countMultiSts(closes, mins, maxs);
			final double[] stss = stsRet[0];
			final double[] stssEma = stsRet[1];

			LOGGER.debug(MessageFormat.format("{0}|Statistics calculated", company.getSymbol()));

			// iterate over all quotes starting from oldest, count from
			// sixth quote because this is EMA 5 first count
			int startIndex = closes.length - 6;
			for (int i = startIndex; i >= 0; i--) {

				DailyQuoteRecord quote = quotes.get(i);
				StatisticRecord statistic = new StatisticRecord();
				statistic.setQuote(quote);
				statistic.setCompany(company);

				if (emas5.length > i) {
					statistic.setEma5(emas5[i]);
				}
				if (emas10.length > i) {
					statistic.setEma10(emas10[i]);
				}
				if (emas12.length > i) {
					statistic.setEma12(emas12[i]);
				}
				if (emas14.length > i) {
					statistic.setEma14(emas14[i]);
				}
				if (emas20.length > i) {
					statistic.setEma20(emas20[i]);
				}
				if (emas26.length > i) {
					statistic.setEma26(emas26[i]);
				}
				if (emas50.length > i) {
					statistic.setEma50(emas50[i]);
				}
				if (emas100.length > i) {
					statistic.setEma100(emas100[i]);
				}
				if (avVol5.length > i) {
					statistic.setAverageVol5((int) avVol5[i]);
				}
				if (avVol12.length > i) {
					statistic.setAverageVol12((int) avVol12[i]);
				}
				if (avVol26.length > i) {
					statistic.setAverageVol26((int) avVol26[i]);
				}
				if (avVol50.length > i) {
					statistic.setAverageVol50((int) avVol50[i]);
				}
				if (smas14.length > i) {
					statistic.setSma14(smas14[i]);
				}
				if (smas28.length > i) {
					statistic.setSma28(smas28[i]);
				}
				if (smas42.length > i) {
					statistic.setSma42(smas42[i]);
				}
				if (macds.length > i) {
					statistic.setMacd(macds[i]);
				}
				if (macdsEma.length > i) {
					statistic.setMacdEma(macdsEma[i]);
				}
				if (rocs.length > i) {
					statistic.setRoc(rocs[i]);
				}
				if (rocsEma.length > i) {
					statistic.setSroc(rocsEma[i]);
				}
				if (rsis.length > i) {
					statistic.setRsi(rsis[i]);
				}
				if (atrs.length > i) {
					statistic.setAtr(atrs[i]);
				}
				if (dmiPs.length > i) {
					statistic.setDmiPlus(dmiPs[i]);
				}
				if (dmiMs.length > i) {
					statistic.setDmiMinus(dmiMs[i]);
				}
				if (adxs.length > i) {
					statistic.setAdx(adxs[i]);
				}
				if (stss.length > i) {
					statistic.setSts(stss[i]);
				}
				if (stssEma.length > i) {
					statistic.setStsEma(stssEma[i]);
				}

				statistic.setAddDate(new Date());
				final Long statisticId = statisticService.add(statistic);
				LOGGER.debug(MessageFormat.format("{0}|Statistic added:{1}", company.getSymbol(), statisticId));
			}

			LOGGER.info(MessageFormat.format("{0}|Statistics stored", company.getSymbol()));
		} catch (Exception e) {
			LOGGER.error("ERR.CALC.STAT." + companySymbol, e);
		}

	}

	/**
	 * Get all statistic from requested period (rather not used)
	 * @param date - quote date
	 * @return
	 */
	public List<StatisticRecordSimple> processStatisticListRequest(Date date) {
		return getStatisticList(date, null);
	}

	/**
	 * Get statistic for companies in the list from requested period
	 * @param date - quote date
	 * @param ids - list of companies
	 * @return
	 */
	public List<StatisticRecordSimple> processStatisticListRequest(Date date, String ids) {
		// create data table
		String[] idsTmp = ids.split(",");
		Integer[] idsTable = new Integer[idsTmp.length];
		for (int i = 0; i < idsTable.length; i++) {
			idsTable[i] = Integer.valueOf(idsTmp[i]);
		}

		// convert result
		return getStatisticList(date, idsTable);
	}

	/**
	 * Get statistic for companies in the stock index from requested period
	 * @param date - quote date
	 * @param groupId - index id
	 * @return
	 */
	public List<StatisticRecordSimple> processGroupRequest(Date date, Integer groupId) {
		// collect index companies
		StockIndex index = indexService.load(groupId);
		List<Company> companies = index.getCompanies();
		Integer[] ids = new Integer[companies.size()];
		for (int i = 0; i < companies.size(); i++) {
			ids[i] = companies.get(i).getId();
		}

		// convert result
		return getStatisticList(date, ids);
	}

	/**
	 * Get statistic for company from requested period
	 * @param date - quote date
	 * @param companyId - company id
	 * @return
	 */
	public StatisticDetails processDetailsRequest(Date date, Integer companyId) {
		// collect data
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, -30);
		Company company = companyService.load(companyId);
		DailyQuoteRecord quote = quoteService.findLastByCompany(company);
		List<DailyQuoteRecord> quotes = quoteService.findByDateAndIds(calendar.getTime(), date, new Integer[] { companyId });

		// creating and returning response
		StatisticDetails details = new StatisticDetails();
		details.setQuote(new pl.stock.data.dto.DailyQuoteRecord(quote));
		details.setStatistic(conversionService.createStatisticSimple(quotes));
		details.setFull(new pl.stock.data.dto.StatisticRecord(quotes.get(0).getStatistic()));
		return details;
	}

	/**
	 * Store update information with current date
	 */
	public void saveUpdate(final pl.stock.data.beans.UpdateType type) {
		final UpdateHistory history = new UpdateHistory();
		history.setAddDate(new Date());
		history.setStatus(UpdateStatus.SUCCESS);
		history.setType(type);
		final Integer id = updateService.add(history);
		LOGGER.info(MessageFormat.format("Update {0} added", id));
	}

	/**
	 * Add new index with name
	 * @param name
	 */
	public void createIndexIfNotExists(final String name) {
		if (indexService.findByName(name) == null) {
			final StockIndex index = new StockIndex();
			index.setName(name);
			final Integer id = indexService.add(index);
			LOGGER.info(MessageFormat.format("Index {0} added", id));
		}
	}

	/**
	 * Return last update add date
	 * @return
	 */
	public Date getLastUpDate() {
		return updateService.findNewest().getAddDate();
	}

	/**
	 * Collect statistics for company list and convert to DTOs
	 * @param date - requested quote date
	 * @param ids - list of companies to collect (optional)
	 */
	private List<StatisticRecordSimple> getStatisticList(Date date, Integer[] ids) {
		// get 10 days before requested date for statistic calculation
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, -30);

		// collect data, all records or filtered by parameter
		List<StatisticRecordSimple> result = new ArrayList<StatisticRecordSimple>();
		List<DailyQuoteRecord> quotes = null;
		if (ids != null) {
			if (ids.length > 0)
				quotes = quoteService.findByDateAndIds(calendar.getTime(), date, ids);
			else
				return result;
		} else {
			quotes = quoteService.findByDatePeriod(calendar.getTime(), date);
		}

		// convert result
		if (quotes.size() > 0) {
			Company company = quotes.get(0).getCompany();
			List<DailyQuoteRecord> companyStats = new ArrayList<DailyQuoteRecord>();
			for (DailyQuoteRecord quote : quotes) {
				if (!quote.getCompany().equals(company)) {
					result.add(conversionService.createStatisticSimple(companyStats));
					companyStats.clear();
				}
				companyStats.add(quote);
				company = quote.getCompany();
			}

			// add last result and return result
			result.add(conversionService.createStatisticSimple(companyStats));
		}
		
		// result
		return result;
	}

}
