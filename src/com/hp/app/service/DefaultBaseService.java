package com.hp.app.service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Hibernate;

import com.hp.app.dao.IBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseServiceException;

public class DefaultBaseService<T, PK extends Serializable> implements
		IBaseService<T, PK> {

	protected IBaseDao<T, PK> objectDao;

	public DefaultBaseService() {
	}

	public DefaultBaseService(IBaseDao<T, PK> dao) {
		this.objectDao = dao;
	}

	public PK create(T newInstance) throws BaseServiceException {
		try {
			return this.objectDao.create(newInstance);
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}
	}

	public void delete(T persistentObject) throws BaseServiceException {
		try {
			this.objectDao.delete(persistentObject);
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}
	}

	public static void loadLazyPropertiesForCollection(Collection<?> list,
			String[] properties) {
		if ((list != null) && (list.size() > 0))
			for (Iterator<?> localIterator = list.iterator(); localIterator
					.hasNext();) {
				Object o = localIterator.next();
				loadLazyProperties(o, properties);
			}
	}

	public static void loadLazyProperties(Object o, String[] properties) {
		if ((properties != null) && (properties.length > 0))
			for (String pro : properties)
				try {
					Hibernate.initialize(PropertyUtils.getProperty(o, pro));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
	}

	@SuppressWarnings("unchecked")
	public T read(PK id, String[] properties) throws BaseServiceException {
		try {
			Object ret = this.objectDao.read(id);
			loadLazyProperties(ret, properties);
			return (T) ret;
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}
	}

	public void update(T transientObject) throws BaseServiceException {
		try {
			this.objectDao.update(transientObject);
		} catch (BaseDaoException e) {
			throw new BaseServiceException(e);
		}
	}

	public IBaseDao<T, PK> getObjectDao() {
		return this.objectDao;
	}

	public void setObjectDao(IBaseDao<T, PK> objectDao) {
		this.objectDao = objectDao;
	}
}
