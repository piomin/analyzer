package pl.stock.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.piomin.core.data.generic.GenericServiceImpl;
import pl.stock.data.dao.UpdateHistoryDao;
import pl.stock.data.entity.UpdateHistory;
import pl.stock.data.service.UpdateHistoryService;

@Service
@Transactional
public class UpdateHistoryServiceImpl extends GenericServiceImpl<Integer, UpdateHistory> implements UpdateHistoryService {

	@Autowired
	public void setUpdateHistoryDao(UpdateHistoryDao updateHistoryDao) {
		this.dao = updateHistoryDao;
	}

	@Override
	public UpdateHistory findNewest() {
		return ((UpdateHistoryDao) this.dao).findNewest();
	}

}
