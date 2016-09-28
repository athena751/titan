package com.hp.app.dao;

import java.io.Serializable;

import com.hp.app.exception.BaseDaoException;

public abstract interface IBaseDao<T, PK extends Serializable> {

	public abstract PK create(T paramT) throws BaseDaoException;

	public abstract T read(PK paramPK) throws BaseDaoException;

	public abstract void update(T paramT) throws BaseDaoException;

	public abstract void delete(T paramT) throws BaseDaoException;
}
