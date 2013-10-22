package pl.stock.data.service;

import pl.piomin.core.data.generic.GenericService;
import pl.stock.data.beans.UpdateType;
import pl.stock.data.entity.UpdateHistory;

public interface UpdateHistoryService extends GenericService<Integer, UpdateHistory> {

	UpdateHistory findNewest();
	
	UpdateHistory findNewestByType(UpdateType type);
	
	int countByType(UpdateType type);
	
}
