package pl.stock.data.core;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Piotr Mińkowski
 *
 * @param <P> - primary key type
 * @param <T> - entity object type
 */
public interface GenericDao<P extends Serializable, T extends GenericEntity<P>> {

	/**
	 * Returns entity class type
	 * @return
	 */
	Class<T> getEntityClass();
	
	/**
	 * Count number of records
	 * @param c - entity class
	 * @return - number of records
	 */
	int count();
	
	/**
	 * Insert new record
	 * @param entity - entity
	 * @return - primary key
	 */
	P save(T entity);
	
	/**
	 * Select record from table by primary key
	 * @param pk - primary key
	 * @param c - entity class
	 * @return - entity instance
	 */
	T load(P pk);
	
	/**
	 * Select all records from table
	 * @param c - entity class
	 * @return - entities list
	 */
	List<T> loadAll();
	
	/**
	 * Select all records from table
	 * @param c - entity class
	 * @param maxResults - maximum number of results in single page
	 * @param firstResult - offset
	 * @return - entities list
	 */
	List<T> loadAllWithPagination(int maxResults, int firstResult);
	
	/**
	 * Remove record from table
	 * @param t - removed entity
	 */
	void delete(T t);
	
	/**
	 * Merge detached entity
	 * @param t - entity to be merged
	 * @return
	 */
	T merge(T t);
	
	/**
	 * Update record
	 * @param t
	 */
	void update(T t);
	
}
