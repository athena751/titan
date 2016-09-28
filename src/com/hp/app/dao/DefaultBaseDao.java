package com.hp.app.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.hp.app.exception.BaseDaoException;

public class DefaultBaseDao<T, PK extends Serializable> extends
		HibernateDaoSupport implements IBaseDao<T, PK> {

	protected Class<T> type;

	@SuppressWarnings("unchecked")
	public DefaultBaseDao() {
		this.type = ((Class) ((java.lang.reflect.ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0]);
	}

	@SuppressWarnings("unchecked")
	public PK create(T newInstance) throws BaseDaoException {
		return (PK) getHibernateTemplate().save(newInstance);
	}

	public void delete(T persistentObject) throws BaseDaoException {
		getHibernateTemplate().delete(persistentObject);
	}

	@SuppressWarnings("unchecked")
	public T read(PK id) throws BaseDaoException {
		return (T) getHibernateTemplate().get(this.type, id);
	}

	public void update(T transientObject) throws BaseDaoException {
		getHibernateTemplate().update(transientObject);
	}

	@SuppressWarnings("unchecked")
	public boolean delete(PK id) throws BaseDaoException {
		Object entity = read(id);
		if (entity == null) {
			return false;
		}
		delete((T) entity);
		return true;
	}

	public void saveOrUpdate(T entity) throws BaseDaoException {
		getHibernateTemplate().saveOrUpdate(entity);
	}
	/**
	 * 处理此类hql: select count(model) from model
	 * 
	 * @param hql
	 * @return
	 */
	protected Object findCountByHql(final String hql) {
		return (Object) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				return session.createQuery(hql).uniqueResult();
			}
		});
	}
	
	  protected List findAllByCriteria(final DetachedCriteria detachedCriteria) {
		  return (List) getHibernateTemplate().executeWithNativeSession(
				  new HibernateCallback() {
					  public Object doInHibernate(Session session) throws HibernateException {
						  Criteria criteria = detachedCriteria.getExecutableCriteria(session);
						  return criteria.list();
					  }
				  });
	  }
}
