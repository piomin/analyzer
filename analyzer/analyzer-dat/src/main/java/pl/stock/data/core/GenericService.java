package pl.stock.data.core;

import java.io.Serializable;
import java.util.List;

/**
 * Generic interface for data services  
 * @author Piotr Mińkowski
 *
 * @param <P> - entity primary key type 
 */
public interface GenericService<P extends Serializable, T extends GenericEntity<P>> {

	/**
	 * Count number of entities 
	 * @return - number of entities
	 */
	public int count();
	
	/**
	 * Add new entity service
	 * @param entity - entity
	 * @return - entity with primary key
	 */
	public P add(T entity);

	/**
	 * Add or update entity if exists
	 * @param entity - entity
	 * @return - entity with primary key
	 */
	public T save(T entity);

	/**
	 * Remove entity service
	 * @param entity - entity
	 */
	public void remove(T entity);

	/**
	 * Find entity by primary key service
	 * @param pk - primary key
	 * @return - found entity
	 */
	public T load(P pk);

	/**
	 * Load all existing entities service
	 * @return - list with entities
	 */
	public List<T> loadAll();

	/**
	 * Load all existing entities with criteria
	 * @param maxResults - maximum number of results
	 * @param firstResult - offset
	 * @return - list with entities
	 */
	public List<T> loadAllWithPagination(int maxResults, int firstResult);

}
