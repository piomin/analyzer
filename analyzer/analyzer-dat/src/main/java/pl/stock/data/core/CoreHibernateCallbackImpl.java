package pl.stock.data.core;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

public class CoreHibernateCallbackImpl implements HibernateCallback<List<Serializable>> {

	private String queryString;

	private String[] paramNames;

	private Object[] values;

	private int firstResult;

	private int maxResults;

	public CoreHibernateCallbackImpl(String queryString, int firstResult, int maxResults) {
		this.queryString = queryString;
		this.firstResult = firstResult;
		this.maxResults = maxResults;
	}

	public CoreHibernateCallbackImpl(String queryString, String[] paramNames, Object[] values, int firstResult, int maxResults) {
		this.queryString = queryString;
		this.paramNames = paramNames;
		this.values = values;
		this.firstResult = firstResult;
		this.maxResults = maxResults;
	}

	@Override
	public List<Serializable> doInHibernate(Session session) throws HibernateException, SQLException {
		Query query = session.createQuery(queryString);
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);

		if (paramNames != null) {
			for (int c = 0; c < paramNames.length; c++) {
				query.setParameter(paramNames[c], values[c]);
			}
		}

		@SuppressWarnings("unchecked")
		List<Serializable> result = query.list();

		return result;
	}

}
