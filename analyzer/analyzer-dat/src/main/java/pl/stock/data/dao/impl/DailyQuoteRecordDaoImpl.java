package pl.stock.data.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import pl.stock.data.core.GenericDaoImpl;
import pl.stock.data.dao.DailyQuoteRecordDao;
import pl.stock.data.entity.Company;
import pl.stock.data.entity.DailyQuoteRecord;

/**
 * Class with DAO operations for DailyQuoteRecord entity
 * @author Piotr Mińkowski
 *
 */
@Repository
public class DailyQuoteRecordDaoImpl extends GenericDaoImpl<Long> implements DailyQuoteRecordDao {

	public DailyQuoteRecord findLastByCompany(Company company) {
		return (DailyQuoteRecord) this.getCurrentSession()
				.createQuery("select a from DailyQuoteRecord a left join fetch a.statistic c where a.id = (select max(id) from DailyQuoteRecord b where b.company = :company)").setEntity("company", company)
				.uniqueResult();
	}

	public int countByCompany(Company company) {
		return ((Long) this.getCurrentSession().createQuery("select count(id) from DailyQuoteRecord a where a.company = :company").setEntity("company", company).uniqueResult())
				.intValue();
	}
	
	@SuppressWarnings("unchecked")
	public List<DailyQuoteRecord> findByCompany(Company company, int maxCount) {
		final String query = "select r from DailyQuoteRecord r where r.company = :company order by r.date desc";
		return this.getCurrentSession().createQuery(query).setMaxResults(maxCount).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<DailyQuoteRecord> findAllByCompany(Company company) {
		final String query = "select r from DailyQuoteRecord r where r.company = :company order by r.date desc";
		return this.getCurrentSession().createQuery(query).setEntity("company", company).list();
	}
	
}
