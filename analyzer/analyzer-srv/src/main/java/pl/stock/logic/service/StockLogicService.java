package pl.stock.logic.service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import pl.stock.data.beans.UpdateStatus;
import pl.stock.data.entity.Company;
import pl.stock.data.entity.DailyQuoteRecord;
import pl.stock.data.entity.StatisticRecord;
import pl.stock.data.entity.UpdateHistory;
import pl.stock.data.service.CompanyService;
import pl.stock.data.service.DailyQuoteRecordService;
import pl.stock.data.service.StatisticRecordService;
import pl.stock.data.service.UpdateHistoryService;

@Service
@Transactional(readOnly = false)
@TransactionConfiguration(defaultRollback = false)
public class StockLogicService {

	// log4j object
	private static final Logger LOGGER = Logger.getLogger(StockLogicService.class);
	
	// company service object
	@Autowired
	private CompanyService companyService;
	
	// daily quote record service object
	@Autowired
	private DailyQuoteRecordService quoteService;
	
	// statistic record service object
	@Autowired
	private StatisticRecordService statisticService;
	
	// update history service object
	@Autowired
	private UpdateHistoryService updateService;
	
	// calculation service object
	@Autowired
	private CalculationService calculationService;
	
	
	/**
	 * Process daily file update schedule 
	 * @param records - list of records read from file
	 */
	public void processDailyUpdate(final List<DailyQuoteRecord> records) {
		
		// number of records added
		int counter = 0;
		
		// iterating over all daily read records
		for (DailyQuoteRecord record : records) {
			
			// set company get from database in DailyQuoteRecord
			final String symbol = record.getCompany().getSymbol();
			Company company = companyService.findBySymbol(symbol);
			if (company == null) {
				company = new Company(symbol, symbol);
				Integer id = companyService.add(company);
				company.setId(id);
			}
			record.setCompany(company);
			LOGGER.info(MessageFormat.format("{0} | Added id -> {1} ", symbol, company.getId()));
			
			// save daily quote record in database
			final Long recordId = quoteService.add(record);
			record.setPrimaryKey(recordId);
			LOGGER.info(MessageFormat.format("{0} | Daily quote {1} added", symbol, recordId));
			counter++;
		}
		
		// save information about update status in database
		final UpdateHistory history = new UpdateHistory();
		history.setAddDate(new Date());
		history.setStatus(UpdateStatus.SUCCESS);
		updateService.add(history);
		LOGGER.info(MessageFormat.format("{0} quotes have been added", counter));
		
	}
	
	
	/**
	 * Check if it is initial update
	 * @return - true if it is initial update
	 */
	public boolean checkIfInitialUpdate() {
		if (updateService.count() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * Process daily statistic calculation for all companies
	 */
	public void processDailyCalculation() {
		
		// load all actual companies from database
		final List<Company> companies = (List<Company>) companyService.loadAll();

		// getting today date
		final Date today = new Date();
		
		// iterate over all companies 
		for (Company company : companies) {
			
			// find daily quote history and last statistic record from database
			List<DailyQuoteRecord> quotes = quoteService.findByCompany(company, 100);
			DailyQuoteRecord lastQuote = quotes.get(0);
			StatisticRecord previousStatistic = quotes.get(1).getStatistic();		
			
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
			actualStatistic.setAddDate(today);
			
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
			actualStatistic.setSma14(calculationService.countSma(14, closes));
			actualStatistic.setSma28(calculationService.countSma(28, closes));
			actualStatistic.setSma42(calculationService.countSma(42, closes));
			
			// calculate RSI for standard period (14 days)
			actualStatistic.setRsi(calculationService.countRsi(closes));
			
			// calculate STS for standard period (9, 5 days)
			final double[] sts = calculationService.countSingleSts(closes, mins, maxs, previousStatistic.getStsEma());
			actualStatistic.setSts(sts[0]);
			actualStatistic.setStsEma(sts[1]);
			
			// calculate ATR for standard period
			actualStatistic.setAtr(calculationService.countSingleAtr(closes[1], lastQuote.getMin(), lastQuote.getMax(), previousStatistic.getAtr()));
			
			// calculate MACD for standard period 12, 26 and 14
			final double[] macd = calculationService.countSingleMacd(lastQuote.getClose(), previousStatistic.getEma12(), previousStatistic.getEma26(), previousStatistic.getMacdEma());
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
			LOGGER.info(MessageFormat.format("{0} | Statistic {1} added", company.getSymbol(), statisticId));
		}	
	}
	
	public void processInitialCalculation() {

		// TODO - statistics should be cleaned in the beginning
//		statisticDAO.removeAll();
		
		// load all actual companies from database
		final List<Company> companies = (List<Company>) companyService.loadAll();
		
		// iterate over all companies 
		for (Company company : companies) {
			
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
				
				// TODO - count SMA values
//				float[] smas14 = new float[closes.length - 13];
//				float[] smas28 = new float[closes.length - 27];
//				float[] smas42 = new float[closes.length - 41];				
//				for (int i = 0; i < smas14.length; i++) 
//					smas14[i] = MovingAverage.sma(14, closes);
//				for (int i = 0; i < smas28.length; i++) 
//					smas28[i] = MovingAverage.sma(28, closes);
//				for (int i = 0; i < smas42.length; i++) 
//					smas42[i] = MovingAverage.sma(42, closes);
	
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
				
				// iterate over all quotes starting from oldest, count from sixth quote because this is EMA 5 first count
				int startIndex = closes.length - 6;
				for (int i = startIndex; i >= 0; i--) {
					
					DailyQuoteRecord quote = quotes.get(i);
					
					StatisticRecord statistic = new StatisticRecord();
					statistic.setQuote(quote);
					
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
						statistic.setAverageVol5((int)avVol5[i]);
					}
					if (avVol12.length > i) {
						statistic.setAverageVol12((int)avVol12[i]);
					}
					if (avVol26.length > i) {
						statistic.setAverageVol26((int)avVol26[i]);
					}
					if (avVol50.length > i) {
						statistic.setAverageVol50((int)avVol50[i]);
					}
					
					// TODO - count and set SMA
//					if (smas14.length > i) statistic.setSma14(smas14[i]);
//					if (smas28.length > i) statistic.setSma28(smas28[i]);
//					if (smas42.length > i) statistic.setSma42(smas42[i]);
					
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
					LOGGER.info(MessageFormat.format("{0} | Statistic {1} added", company.getSymbol(), statisticId));
				}
			
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Error for company: " + company.getName());
			}
		}
	}
}
