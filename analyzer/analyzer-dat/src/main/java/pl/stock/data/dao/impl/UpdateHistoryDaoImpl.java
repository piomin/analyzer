package pl.stock.data.dao.impl;

import org.springframework.stereotype.Repository;

import pl.piomin.core.data.generic.GenericDaoImpl;
import pl.stock.data.dao.UpdateHistoryDao;
import pl.stock.data.entity.UpdateHistory;

/**
 * Class with DAO operations for UpdateHistory entity
 * @author Piotr Mińkowski
 *
 */
@Repository
public class UpdateHistoryDaoImpl extends GenericDaoImpl<Integer, UpdateHistory> implements UpdateHistoryDao {

	public UpdateHistoryDaoImpl() {
		super(UpdateHistory.class);
	}

	@Override
	public UpdateHistory findNewest() {
		return (UpdateHistory) this.getSessionFactory().getCurrentSession()
				.createQuery("select a from UpdateHistory a where a.id = (select max(id) from UpdateHistory b)").uniqueResult();
	}
	
}
