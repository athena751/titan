package com.hp.app.dao;

import java.io.Serializable;

public interface IPersistentObject<T extends Serializable> {

	public abstract T getId();

	public abstract void setId(T paramT);

	public abstract Integer getVersion();

	public abstract void setVersion(Integer paramInteger);
}
