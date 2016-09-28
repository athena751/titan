package com.hp.app.dao;

import java.io.Serializable;

public class AbstractPersistenObject<T extends Serializable> implements
		IPersistentObject<T> {

	private T id;
	private Integer version;

	public T getId() {
		return this.id;
	}

	public void setId(T id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if ((o == null) || (!(o instanceof IPersistentObject<?>))) {
			return false;
		}

		IPersistentObject<?> other = (IPersistentObject<?>) o;

		if (this.id == null) {
			return false;
		}

		return this.id.equals(other.getId());
	}

	public int hashCode() {
		if (this.id != null) {
			return this.id.hashCode();
		}
		return super.hashCode();
	}

	public String toString() {
		return getClass().getName() + "[id=" + this.id + "]";
	}
}
