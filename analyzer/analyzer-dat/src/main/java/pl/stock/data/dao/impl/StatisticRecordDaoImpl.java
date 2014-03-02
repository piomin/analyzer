package pl.stock.data.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import pl.piomin.core.data.generic.GenericDaoImpl;
import pl.stock.data.dao.StatisticRecordDao;
import pl.stock.data.entity.DailyQuoteRecord;
import pl.stock.data.entity.StatisticRecord;

/**
 * Class with DAO operations for StatisticRecord entity
 * 
 * @author Piotr Mińkowski
 * 
 */
@Repository
public class StatisticRecordDaoImpl extends GenericDaoImpl<Long, StatisticRecord> implements StatisticRecordDao {

	public StatisticRecordDaoImpl() {
		super(StatisticRecord.class);
	}

	public StatisticRecord findLastByQuote(DailyQuoteRecord quote) {
		return (StatisticRecord) this.getSessionFactory().getCurrentSession()
				.createQuery("select a from StatisticRecord a where a.id = (select max(id) from StatisticRecord b where b.quote = :quote)").setEntity("quote", quote)
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<StatisticRecord> findByDate(Date date) {
		return this.getSessionFactory().getCurrentSession().createQuery("select a from StatisticRecord a where a.addDate = :date").list();
	}

	@SuppressWarnings("unchecked")
	public List<StatisticRecord> findByDatePeriod(Date from, Date to) {
		return this.getSessionFactory().getCurrentSession()
				.createQuery("select a from StatisticRecord a join fetch a.quote b where b.date between :from and :to order by a.company.id asc, b.date desc")
				.setDate("from", from).setDate("to", to).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StatisticRecord> findByDateAndIds(Date from, Date to, Integer[] companyIds) {
		return this
				.getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"select a from StatisticRecord a left join fetch a.quote b left join fetch a.company c left join fetch b.company d where b.date between :from and :to and c.id in (:ids) order by c.id asc, b.date desc")
				.setDate("from", from).setDate("to", to).setParameterList("ids", companyIds).list();
	}

}
