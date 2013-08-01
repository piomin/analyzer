package pl.stock.data.core;

import java.io.Serializable;
import java.util.List;


public abstract class GenericServiceImpl<P extends Serializable, T extends GenericEntity<P>> implements GenericService<P, T> {

	protected GenericDao<P, T> dao;
	
	@Override
	public int count() {
		return dao.count();
	}
	
	@Override
	public P add(T entity) {
		return dao.save(entity);
	}

	@Override
	public T save(T entity) {
		if (entity.getPrimaryKey() == null) {
			P pk = dao.save(entity);
			entity.setPrimaryKey(pk);
		} else
			entity = dao.merge(entity);
		return entity;
	}

	@Override
	public void remove(T entity) {
		dao.delete(entity);	
	}

	@Override
	public T load(P pk) {
		return dao.load(pk);
	}

	@Override
	public List<T> loadAll() {
		return dao.loadAll();
	}
	
	public List<T> loadAllWithPagination(int maxResults, int firstResult) {
		return dao.loadAllWithPagination(maxResults, firstResult);
	}

}
