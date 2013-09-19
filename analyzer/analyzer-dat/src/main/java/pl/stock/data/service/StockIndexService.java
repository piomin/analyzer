package pl.stock.data.service;

import pl.piomin.core.data.generic.GenericService;
import pl.stock.data.entity.StockIndex;

public interface StockIndexService extends GenericService<Integer, StockIndex> {

	StockIndex findByName(final String name);
	
}
