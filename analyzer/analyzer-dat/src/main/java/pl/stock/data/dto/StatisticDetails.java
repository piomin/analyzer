package pl.stock.data.dto;

import pl.stock.data.dto.DailyQuoteRecord;
import pl.stock.data.dto.StatisticRecordSimple;

public class StatisticDetails {

	private DailyQuoteRecord quote;
	private StatisticRecordSimple statistic;
	private StatisticRecord full;

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

	public StatisticRecord getFull() {
		return full;
	}

	public void setFull(StatisticRecord full) {
		this.full = full;
	}

}
