package pl.stock.data.dao;

import pl.piomin.core.data.generic.GenericDao;
import pl.stock.data.entity.Company;

public interface CompanyDao extends GenericDao<Integer, Company> {

	/**
	 * Find company by symbol name
	 * @param symbol - symbol
	 * @return - Company entity
	 */
	Company findBySymbol(final String symbol);
	
}
