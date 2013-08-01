package pl.stock.data.core;

import java.io.Serializable;
import java.util.List;


public abstract class GenericServiceImpl<P extends Serializable> implements GenericService<P> {

	protected GenericDao<P> dao;
	
	@Override
	public int count(Class<? extends Serializable> c) {
		return dao.count(c);
	}
	
	@Override
	public P add(GenericEntity<P> entity) {
		return dao.save(entity);
	}

	@Override
	public GenericEntity<P> save(GenericEntity<P> entity) {
		if (entity.getPrimaryKey() == null) {
			P pk = dao.save(entity);
			entity.setPrimaryKey(pk);
		} else
			entity = dao.merge(entity);
		return entity;
	}

	@Override
	public void remove(GenericEntity<P> entity) {
		dao.delete(entity);	
	}

	@SuppressWarnings("unchecked")
	@Override
	public GenericEntity<P> load(P pk, Class<? extends Serializable> c) {
		return dao.load(pk, (Class<Serializable>)c);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<? extends Serializable> loadAll(Class<? extends Serializable> c) {
		return dao.loadAll((Class<Serializable>) c);
	}
	
	@SuppressWarnings("unchecked")
	public List<Serializable> loadAllWithPagination(Class<? extends Serializable> c, int maxResults, int firstResult) {
		return dao.loadAllWithPagination((Class<Serializable>) c, maxResults, firstResult);
	}

}
