package pl.stock.data.service;

import pl.stock.data.core.GenericService;
import pl.stock.data.entity.DailyQuoteRecord;
import pl.stock.data.entity.StatisticRecord;

public interface StatisticRecordService extends GenericService<Long> {

	StatisticRecord findLastByQuote(DailyQuoteRecord quote);
	
}
