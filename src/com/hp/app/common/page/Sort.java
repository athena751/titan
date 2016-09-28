package com.hp.app.common.page;

import java.io.Serializable;

public class Sort implements Serializable {
	private static final long serialVersionUID = 5226214578036025603L;

	private String propertyName;
	private Boolean ascending = false;
	
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public Boolean getAscending() {
		return ascending;
	}
	public void setAscending(Boolean ascending) {
		this.ascending = ascending;
	}
}
