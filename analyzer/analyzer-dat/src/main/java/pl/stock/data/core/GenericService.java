package pl.stock.data.core;

import java.io.Serializable;
import java.util.List;

/**
 * Generic interface for data services  
 * @author Piotr Mińkowski
 *
 * @param <P> - entity primary key type 
 */
public interface GenericService<P extends Serializable> {

	/**
	 * Count number of entities
	 * @param c - antity class 
	 * @return - number of entities
	 */
	public int count(Class<? extends Serializable> c);
	
	/**
	 * Add new entity service
	 * @param entity - entity
	 * @return - entity with primary key
	 */
	public P add(GenericEntity<P> entity);

	/**
	 * Add or update entity if exists
	 * @param entity - entity
	 * @return - entity with primary key
	 */
	public GenericEntity<P> save(GenericEntity<P> entity);

	/**
	 * Remove entity service
	 * @param entity - entity
	 */
	public void remove(GenericEntity<P> entity);

	/**
	 * Find entity by primary key service
	 * @param pk - primary key
	 * @param c - searched object type
	 * @return - found entity
	 */
	public GenericEntity<P> load(P pk, Class<? extends Serializable> c);

	/**
	 * Load all existing entities service
	 * @param c - searched object type
	 * @return - list with entities
	 */
	public List<? extends Serializable> loadAll(Class<? extends Serializable> c);

	/**
	 * Load all existing entities with criteria
	 * @param c - searched object type 
	 * @param maxResults - maximum number of results
	 * @param firstResult - offset
	 * @return - list with entities
	 */
	public List<Serializable> loadAllWithPagination(Class<? extends Serializable> c, int maxResults, int firstResult);

}
