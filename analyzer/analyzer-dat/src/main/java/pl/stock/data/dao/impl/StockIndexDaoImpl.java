package pl.stock.data.dao.impl;

import org.springframework.stereotype.Repository;

import pl.piomin.core.data.generic.GenericDaoImpl;
import pl.stock.data.dao.StockIndexDao;
import pl.stock.data.entity.StockIndex;

@Repository
public class StockIndexDaoImpl extends GenericDaoImpl<Integer, StockIndex> implements StockIndexDao {

	public StockIndexDaoImpl() {
		super(StockIndex.class);
	}
	
	public StockIndex findByName(final String name) {
		return (StockIndex) this.getSessionFactory().getCurrentSession().createQuery("from StockIndex i where i.name = :name").setString("name", name).uniqueResult();
	}
	
}
