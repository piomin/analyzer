package pl.stock.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.stock.data.core.GenericServiceImpl;
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
	
}
