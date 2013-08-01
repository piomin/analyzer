package pl.stock.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.stock.data.core.GenericServiceImpl;
import pl.stock.data.dao.UpdateHistoryDao;
import pl.stock.data.service.UpdateHistoryService;

@Service
@Transactional
public class UpdateHistoryServiceImpl extends GenericServiceImpl<Integer> implements UpdateHistoryService {

	@Autowired
	public void setUpdateHistoryDao(UpdateHistoryDao updateHistoryDao) {
		this.dao = updateHistoryDao;
	}
	
}
