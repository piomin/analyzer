package pl.stock.logic.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import pl.stock.data.beans.SignalStatus;
import pl.stock.data.dto.StatisticRecordSimple;
import pl.stock.data.entity.StatisticRecord;
import pl.stock.logic.helper.CalculationHelper;

@Service
public class ConversionService {

	/**
	 * Create message for list information
	 * @param statistics
	 * @return
	 */
	public StatisticRecordSimple createStatisticSimple(List<StatisticRecord> statistics) {
		StatisticRecordSimple result = new StatisticRecordSimple();
		StatisticRecord last = statistics.get(0);
		StatisticRecord previous = statistics.get(1);
		SignalStatus signal = null;

		// fill RSI, ADX
		result.setRsi(new BigDecimal(last.getRsi()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		result.setAdx(new BigDecimal(last.getAdx()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		result.setSts(new BigDecimal(last.getSts()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

		// detect MACD signal
		signal = CalculationHelper.detectSignalType(new double[] { last.getMacd(), previous.getMacd() }, new double[] { last.getMacdEma(), previous.getMacdEma() },
				0.02);
		result.setMacdStatus(signal);

		// detect DMI signal
		signal = CalculationHelper.detectSignalType(new double[] { last.getDmiPlus(), previous.getDmiPlus() },
				new double[] { last.getDmiMinus(), previous.getDmiMinus() }, 0.05);
		result.setDmiStatus(signal);

		// detect EMA50, close signal
		signal = CalculationHelper.detectSignalType(new double[] { last.getQuote().getClose(), previous.getQuote().getClose() }, new double[] { last.getDmiMinus(),
				previous.getDmiMinus() }, 0.05);
		result.setEma50Status(signal);

		result.setCompanySymbol(last.getQuote().getCompany().getSymbol());
		result.setCompanyId(last.getQuote().getCompany().getId());

		return result;
	}

}
