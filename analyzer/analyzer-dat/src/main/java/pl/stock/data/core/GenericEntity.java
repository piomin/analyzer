package pl.stock.data.core;

import java.io.Serializable;

public abstract class GenericEntity<P extends Serializable> implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public abstract P getPrimaryKey();
	
	public abstract void setPrimaryKey(P pk);
	
	public abstract String getShortInfo();
	
	public void setShortInfo(String shortInfo) {
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof GenericEntity))
			return false;
		GenericEntity<P> entity = (GenericEntity<P>) obj;
		if (entity.getPrimaryKey() == null)
			return false;
		return entity.getPrimaryKey().equals(this.getPrimaryKey());
	}
	
}
