package pl.stock.data.dao;

import pl.stock.data.core.GenericDao;
import pl.stock.data.entity.Company;

public interface CompanyDao extends GenericDao<Integer> {

	/**
	 * Find company by symbol name
	 * @param symbol - symbol
	 * @return - Company entity
	 */
	Company findBySymbol(final String symbol);
	
}
