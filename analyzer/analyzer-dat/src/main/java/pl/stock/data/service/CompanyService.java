package pl.stock.data.service;

import pl.piomin.core.data.generic.GenericService;
import pl.stock.data.entity.Company;

public interface CompanyService extends GenericService<Integer, Company> {

	/**
	 * Find company by symbol name
	 * @param symbol - symbol
	 * @return - Company entity
	 */
	Company findBySymbol(final String symbol);
	
}
