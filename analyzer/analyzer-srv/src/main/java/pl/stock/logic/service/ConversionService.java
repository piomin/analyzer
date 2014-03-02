package pl.stock.logic.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import pl.stock.data.beans.SignalStatus;
import pl.stock.data.dto.StatisticRecordSimple;
import pl.stock.data.entity.DailyQuoteRecord;
import pl.stock.data.entity.StatisticRecord;
import pl.stock.data.entity.StockIndex;
import pl.stock.logic.helper.CalculationHelper;

@Service
public class ConversionService {

	/**
	 * Create message for list information
	 * @param statistics
	 * @return
	 */
	public StatisticRecordSimple createStatisticSimple(List<DailyQuoteRecord> quotes) {
		StatisticRecordSimple result = new StatisticRecordSimple();
		StatisticRecord last = quotes.get(0).getStatistic();
		StatisticRecord previous = quotes.get(1).getStatistic();
		final DailyQuoteRecord lastQuote = quotes.get(0);
		SignalStatus signal = null;

		// fill RSI, ADX, STS, RoC, ATR
		result.setRsi(new BigDecimal(last.getRsi()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		result.setAdx(new BigDecimal(last.getAdx()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		result.setSts(new BigDecimal(last.getSts()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		result.setAtr(new BigDecimal(last.getAtr()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		result.setRoc(new BigDecimal(last.getRoc()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		result.setAtrPercentage(CalculationHelper.countPercentageDiff(last.getAtr(), lastQuote.getClose()));

		// fill volume table
		result.setVolumens(new double[5]);
		result.addVolumen(0, lastQuote.getVolumen());
		result.addVolumen(1, last.getAverageVol5());
		result.addVolumen(2, last.getAverageVol12());
		result.addVolumen(3, last.getAverageVol26());
		result.addVolumen(4, last.getAverageVol50());
		
		// detect MACD signal
		signal = CalculationHelper.detectSignalType(new double[] { last.getMacd(), previous.getMacd() }, new double[] { last.getMacdEma(), previous.getMacdEma() },
				0.02);
		result.setMacdStatus(signal);

		// count prize % changes for 1, 3, 5, 10, 20 days
		result.setPrizeChanges(new double[5]);
		result.addPrizeChange(0, CalculationHelper.countPercentageDiff(previous.getQuote().getClose(), lastQuote.getClose()));
		if (quotes.size() > 3) {
			result.addPrizeChange(1, CalculationHelper.countPercentageDiff(quotes.get(3).getClose(), lastQuote.getClose()));
		}
		if (quotes.size() > 5) {
			result.addPrizeChange(2, CalculationHelper.countPercentageDiff(quotes.get(5).getClose(), lastQuote.getClose()));
		}
		if (quotes.size() > 10) {
			result.addPrizeChange(3, CalculationHelper.countPercentageDiff(quotes.get(10).getClose(), lastQuote.getClose()));
		}
		if (quotes.size() > 20) {
			result.addPrizeChange(4, CalculationHelper.countPercentageDiff(quotes.get(20).getClose(), lastQuote.getClose()));
		}

		// detect DMI signal
		signal = CalculationHelper.detectSignalType(new double[] { last.getDmiPlus(), previous.getDmiPlus() },
				new double[] { last.getDmiMinus(), previous.getDmiMinus() }, 0.05);
		result.setDmiStatus(signal);

		// detect EMAs, closes signal
		result.setEmasStatus(new SignalStatus[4]);
		signal = CalculationHelper.detectSignalType(new double[] { last.getQuote().getClose(), previous.getQuote().getClose() }, new double[] { last.getEma5(),
				previous.getEma5() }, 0.05);
		result.addEmaStatus(0, signal);
		signal = CalculationHelper.detectSignalType(new double[] { last.getQuote().getClose(), previous.getQuote().getClose() }, new double[] { last.getEma10(),
				previous.getEma10() }, 0.05);
		result.addEmaStatus(1, signal);
		signal = CalculationHelper.detectSignalType(new double[] { last.getQuote().getClose(), previous.getQuote().getClose() }, new double[] { last.getEma20(),
				previous.getEma20() }, 0.05);
		result.addEmaStatus(2, signal);
		signal = CalculationHelper.detectSignalType(new double[] { last.getQuote().getClose(), previous.getQuote().getClose() }, new double[] { last.getEma50(),
				previous.getEma50() }, 0.05);
		result.addEmaStatus(3, signal);
		
		// count EMAs percentage
		result.setEmasPercentage(new double[4]);
		result.addEmaPercentage(0, CalculationHelper.countPercentageDiff(last.getEma5(), lastQuote.getClose()));
		result.addEmaPercentage(1, CalculationHelper.countPercentageDiff(last.getEma10(), lastQuote.getClose()));
		result.addEmaPercentage(2, CalculationHelper.countPercentageDiff(last.getEma20(), lastQuote.getClose()));
		result.addEmaPercentage(3, CalculationHelper.countPercentageDiff(last.getEma50(), lastQuote.getClose()));

		// set company data
		result.setCompanySymbol(last.getQuote().getCompany().getSymbol());
		result.setCompanyId(last.getQuote().getCompany().getId());

		return result;
	}

	public List<pl.stock.data.dto.StockIndex> createStockIndexDTOs(List<StockIndex> indices) {
		List<pl.stock.data.dto.StockIndex> dtos = new ArrayList<>();
		for (StockIndex stockIndex : indices) {
			dtos.add(new pl.stock.data.dto.StockIndex(stockIndex.getId(), stockIndex.getName()));
		}
		return dtos;
	}

}
