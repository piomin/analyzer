package pl.stock.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.piomin.core.data.generic.GenericServiceImpl;
import pl.stock.data.dao.StockIndexDao;
import pl.stock.data.entity.StockIndex;
import pl.stock.data.service.StockIndexService;

@Service
@Transactional
public class StockIndexServiceImpl extends GenericServiceImpl<Integer, StockIndex> implements StockIndexService {

	@Override
	public StockIndex findByName(String name) {
		return ((StockIndexDao) dao).findByName(name);
	}
	
	@Autowired
	public void setStockIndexDao(StockIndexDao indexDao) {
		this.dao = indexDao;
	}

}
