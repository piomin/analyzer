package pl.stock.data.core;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;


public class GenericDaoImpl<P extends Serializable> implements GenericDao<P> {

	private SessionFactory sessionFactory;
	
	protected final Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}
	   
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public P save(GenericEntity<P> t) {
		return (P) this.getCurrentSession().save(t);
	}

	@SuppressWarnings("unchecked")
	@Override
	public GenericEntity<P> load(P pk, Class<Serializable> c) {
		return (GenericEntity<P>) this.getCurrentSession().get(c, pk);
	}

	@Override
	public void delete(GenericEntity<P> t) {
		this.getCurrentSession().delete(t);	
	}

	@SuppressWarnings("unchecked")
	@Override
	public GenericEntity<P> merge(GenericEntity<P> t) {
		GenericEntity<P> tNew = (GenericEntity<P>) this.getCurrentSession().merge(t);
		return tNew;
	}

	@Override
	public void update(GenericEntity<P> t) {
		this.getCurrentSession().update(t);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<? extends Serializable> loadAll(Class<? extends Serializable> c) {
		return getCurrentSession().createQuery("from " + c.getName()).list();
	}

	@Override
	public List<Serializable> loadAllWithPagination(Class<Serializable> c, int maxResults, int firstResult) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("select c from ").append(c.getName()).append(" c");
		HibernateCallback<List<Serializable>> callback = new CoreHibernateCallbackImpl(buffer.toString(), firstResult, maxResults);
//		return this.getCurrentSession().executeFind(callback);
		return null;
	}

	@Override
	public int count(Class<? extends Serializable> c) {
		return ((Long) getCurrentSession().createQuery("select count(id) from " + c.getName()).uniqueResult()).intValue();
	}

}
