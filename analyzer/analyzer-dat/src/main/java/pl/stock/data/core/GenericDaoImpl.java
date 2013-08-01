package pl.stock.data.core;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class GenericDaoImpl<P extends Serializable, T extends GenericEntity<P>> implements GenericDao<P, T> {
	
	private SessionFactory sessionFactory;
	private Class<T> entityClass;
	
	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	   
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Class<T> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public P save(T t) {
		return (P) this.getCurrentSession().save(t);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T load(P pk) {
		return (T) this.getCurrentSession().get(this.entityClass, pk);
	}

	@Override
	public void delete(T t) {
		this.getCurrentSession().delete(t);	
	}

	@SuppressWarnings("unchecked")
	@Override
	public T merge(T t) {
		T tNew = (T) this.getCurrentSession().merge(t);
		return tNew;
	}

	@Override
	public void update(T t) {
		this.getCurrentSession().update(t);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> loadAll() {
		return getCurrentSession().createQuery("from " + this.entityClass.getName()).list();
	}

	@Override
	public List<T> loadAllWithPagination(int maxResults, int firstResult) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("select c from ").append(this.entityClass.getName()).append(" c");
		//HibernateCallback<List<Serializable>> callback = new CoreHibernateCallbackImpl(buffer.toString(), firstResult, maxResults);
		//return this.getCurrentSession().executeFind(callback);
		return null;
	}

	@Override
	public int count() {
		return ((Long) getCurrentSession().createQuery("select count(id) from " + this.entityClass.getName()).uniqueResult()).intValue();
	}

}
