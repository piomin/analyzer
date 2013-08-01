package pl.stock.data.service;

import pl.stock.data.core.GenericService;
import pl.stock.data.entity.Company;

public interface CompanyService extends GenericService<Integer> {

	/**
	 * Find company by symbol name
	 * @param symbol - symbol
	 * @return - Company entity
	 */
	Company findBySymbol(final String symbol);
	
}
