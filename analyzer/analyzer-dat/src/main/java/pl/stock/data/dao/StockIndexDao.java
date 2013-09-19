package pl.stock.data.dao;

import pl.piomin.core.data.generic.GenericDao;
import pl.stock.data.entity.StockIndex;

public interface StockIndexDao extends GenericDao<Integer, StockIndex> {

	StockIndex findByName(final String name);
	
}
