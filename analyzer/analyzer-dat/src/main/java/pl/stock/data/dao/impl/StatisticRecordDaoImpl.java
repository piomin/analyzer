package pl.stock.data.dao.impl;

import org.springframework.stereotype.Repository;

import pl.stock.data.core.GenericDaoImpl;
import pl.stock.data.dao.StatisticRecordDao;
import pl.stock.data.entity.DailyQuoteRecord;
import pl.stock.data.entity.StatisticRecord;

/**
 * Class with DAO operations for StatisticRecord entity
 * @author Piotr Mińkowski
 *
 */
@Repository
public class StatisticRecordDaoImpl extends GenericDaoImpl<Long, StatisticRecord> implements StatisticRecordDao {

	public StatisticRecordDaoImpl() {
		setEntityClass(StatisticRecord.class);
	}
	
	public StatisticRecord findLastByQuote(DailyQuoteRecord quote) {
		return (StatisticRecord) this.getCurrentSession()
				.createQuery("select a from StatisticRecord a where a.id = (select max(id) from StatisticRecord b where b.quote = :quote)").setEntity("quote", quote)
				.uniqueResult();
	}

}
