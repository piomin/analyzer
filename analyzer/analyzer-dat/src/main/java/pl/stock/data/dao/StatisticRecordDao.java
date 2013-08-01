package pl.stock.data.dao;

import pl.stock.data.core.GenericDao;
import pl.stock.data.entity.DailyQuoteRecord;
import pl.stock.data.entity.StatisticRecord;

public interface StatisticRecordDao extends GenericDao<Long, StatisticRecord> {

	/**
	 * Find newest record in statistic_record table for requested quote
	 * @param quote - DailyQuoteRecord entity
	 * @return - StatisticRecord entity
	 */
	StatisticRecord findLastByQuote(DailyQuoteRecord quote);
	
}
