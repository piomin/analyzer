package pl.stock.data.core;

import java.io.Serializable;

public class GenericPK<T extends Serializable> {

	private T primaryKey;
	
	/**
	 * Constructor
	 * @param primaryKey - primary key
	 */
	public GenericPK(T primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	/**
	 * Return primary key
	 * @return
	 */
	public T getPrimaryKey() {
		return this.primaryKey;
	}
	
}
