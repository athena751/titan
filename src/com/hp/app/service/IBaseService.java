package com.hp.app.service;

import java.io.Serializable;

import com.hp.app.exception.BaseServiceException;

public abstract interface IBaseService<T, PK extends Serializable> {

	public abstract PK create(T paramT) throws BaseServiceException;

	public abstract T read(PK paramPK, String[] paramArrayOfString)
			throws BaseServiceException;

	public abstract void update(T paramT) throws BaseServiceException;

	public abstract void delete(T paramT) throws BaseServiceException;
}
