package pl.stock.data.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.piomin.core.data.generic.GenericServiceImpl;
import pl.stock.data.dao.StatisticRecordDao;
import pl.stock.data.entity.DailyQuoteRecord;
import pl.stock.data.entity.StatisticRecord;
import pl.stock.data.service.StatisticRecordService;

@Service
@Transactional
public class StatisticRecordServiceImpl extends GenericServiceImpl<Long, StatisticRecord> implements StatisticRecordService {

	@Autowired
	public void setUpdateHistoryDao(StatisticRecordDao statisticRecordDao) {
		this.dao = statisticRecordDao;
	}

	@Override
	public StatisticRecord findLastByQuote(DailyQuoteRecord quote) {
		return ((StatisticRecordDao) this.dao).findLastByQuote(quote);
	}

	@Override
	public List<StatisticRecord> findByDate(Date date) {
		return ((StatisticRecordDao) this.dao).findByDate(date);
	}

	@Override
	public List<StatisticRecord> findByDatePeriod(Date from, Date to) {
		return ((StatisticRecordDao) this.dao).findByDatePeriod(from, to);
	}

	@Override
	public List<StatisticRecord> findByDateAndIds(Date from, Date to, Integer[] companyIds) {
		return ((StatisticRecordDao) this.dao).findByDateAndIds(from, to, companyIds);
	}
	
}
