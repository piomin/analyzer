package pl.stock.data.dto;

import pl.stock.data.dto.DailyQuoteRecord;
import pl.stock.data.dto.StatisticRecordSimple;

public class StatisticDetails {

	private DailyQuoteRecord quote;
	private StatisticRecordSimple statistic;

	public DailyQuoteRecord getQuote() {
		return quote;
	}

	public void setQuote(DailyQuoteRecord quote) {
		this.quote = quote;
	}

	public StatisticRecordSimple getStatistic() {
		return statistic;
	}

	public void setStatistic(StatisticRecordSimple statistic) {
		this.statistic = statistic;
	}

}
