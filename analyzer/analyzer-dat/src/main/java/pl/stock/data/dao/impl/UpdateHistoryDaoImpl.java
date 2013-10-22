package pl.stock.data.dao.impl;

import org.springframework.stereotype.Repository;

import pl.piomin.core.data.generic.GenericDaoImpl;
import pl.stock.data.beans.UpdateType;
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

	@Override
	public UpdateHistory findNewestByType(UpdateType type) {
		return (UpdateHistory) this.getSessionFactory().getCurrentSession()
				.createQuery("select a from UpdateHistory a where a.id = (select max(id) from UpdateHistory b where b.type = :type)").setParameter("type", type)
				.uniqueResult();
	}

	@Override
	public int countByType(UpdateType type) {
		return ((Long) this.getSessionFactory().getCurrentSession().createQuery("select count(id) from UpdateHistory u where u.type = :type")
				.setParameter("type", type).uniqueResult()).intValue();
	}
}
