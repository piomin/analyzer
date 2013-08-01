package pl.stock.data.core;

import java.io.Serializable;
import java.util.List;


public interface GenericDao<P extends Serializable> {

	/**
	 * Count number of records
	 * @param c - entity class
	 * @return - number of records
	 */
	public int count(Class<? extends Serializable> c);
	
	/**
	 * Insert new record
	 * @param entity - entity
	 * @return - primary key
	 */
	public P save(GenericEntity<P> entity);
	
	/**
	 * Select record from table by primary key
	 * @param pk - primary key
	 * @param c - entity class
	 * @return - entity instance
	 */
	public GenericEntity<P> load(P pk, Class<Serializable> c);
	
	/**
	 * Select all records from table
	 * @param c - entity class
	 * @return - entities list
	 */
	public List<? extends Serializable> loadAll(Class<? extends Serializable> c);
	
	/**
	 * Select all records from table
	 * @param c - entity class
	 * @param maxResults - maximum number of results in single page
	 * @param firstResult - offset
	 * @return - entities list
	 */
	public List<Serializable> loadAllWithPagination(Class<Serializable> c, int maxResults, int firstResult);
	
	/**
	 * Remove record from table
	 * @param t - removed entity
	 */
	public void delete(GenericEntity<P> t);
	
	/**
	 * Merge detached entity
	 * @param t - entity to be merged
	 * @return
	 */
	public GenericEntity<P> merge(GenericEntity<P> t);
	
	/**
	 * Update record
	 * @param t
	 */
	public void update(GenericEntity<P> t);
	
}
