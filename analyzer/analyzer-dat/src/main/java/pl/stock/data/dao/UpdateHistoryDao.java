package pl.stock.data.dao;

import pl.piomin.core.data.generic.GenericDao;
import pl.stock.data.beans.UpdateType;
import pl.stock.data.entity.UpdateHistory;

public interface UpdateHistoryDao extends GenericDao<Integer, UpdateHistory> {

	UpdateHistory findNewest();
	
	UpdateHistory findNewestByType(UpdateType type);
	
	int countByType(UpdateType type);
	
}
