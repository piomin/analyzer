package pl.stock.data.dao;

import java.util.List;

import pl.piomin.core.data.generic.GenericDao;
import pl.stock.data.entity.Company;
import pl.stock.data.entity.DailyQuoteRecord;

public interface DailyQuoteRecordDao extends GenericDao<Long, DailyQuoteRecord> {

	/**
	 * Find newest record in daily_quote_record table for requested company
	 * @param company - requested company
	 * @return - DailyQuoteRecord entity
	 */
	DailyQuoteRecord findLastByCompany(Company company);
	
	/**
	 * Count quote records by company name
	 * @param company - requested company
	 * @return
	 */
	int countByCompany(Company company);
	
	/**
	 * Find maxCount quotes starting from newest for requested company
	 * @param company - company entity
	 * @param maxCount - maximum number of records
	 * @return - list of quote entity
	 */
	List<DailyQuoteRecord> findByCompany(Company company, int maxCount);
	
	/**
	 * Find all quotes for company
	 * @param company - company entity
	 * @return - list of quote entity
	 */
	List<DailyQuoteRecord> findAllByCompany(Company company);
	
}
