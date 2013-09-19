package pl.stock.data.dao;

import java.util.Date;
import java.util.List;

import pl.piomin.core.data.generic.GenericDao;
import pl.stock.data.entity.DailyQuoteRecord;
import pl.stock.data.entity.StatisticRecord;

public interface StatisticRecordDao extends GenericDao<Long, StatisticRecord> {

	/**
	 * Find newest record in statistic_record table for requested quote
	 * @param quote - DailyQuoteRecord entity
	 * @return - StatisticRecord entity
	 */
	StatisticRecord findLastByQuote(DailyQuoteRecord quote);

	/**
	 * Find statistic list by date
	 * @param date
	 * @return
	 */
	List<StatisticRecord> findByDate(Date date);

	/**
	 * Find statistic list by date period
	 * @param from
	 * @param to
	 * @return
	 */
	List<StatisticRecord> findByDatePeriod(Date from, Date to);

	/**
	 * Find statistic list by date only for companies in the list
	 * @param from
	 * @param to
	 * @return
	 */
	List<StatisticRecord> findByDateAndIds(Date from, Date to, Integer[] companyIds);

}
