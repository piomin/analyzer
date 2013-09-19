package pl.stock.data.service;

import java.util.Date;
import java.util.List;

import pl.piomin.core.data.generic.GenericService;
import pl.stock.data.entity.DailyQuoteRecord;
import pl.stock.data.entity.StatisticRecord;

public interface StatisticRecordService extends GenericService<Long, StatisticRecord> {

	StatisticRecord findLastByQuote(DailyQuoteRecord quote);
	
	List<StatisticRecord> findByDate(Date date);
	
	List<StatisticRecord> findByDatePeriod(Date from, Date to);
	
	List<StatisticRecord> findByDateAndIds(Date from, Date to, Integer[] companyIds);
	
}
