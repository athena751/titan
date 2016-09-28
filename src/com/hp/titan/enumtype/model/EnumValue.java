package com.hp.titan.enumtype.model;
import java.util.Date;



public class EnumValue  implements java.io.Serializable{

	private Integer enumId;
	private Integer typeId;
	private String enumValue;
	private String description;

	public String getEnumValue() {
		return enumValue;
	}
	public void setEnumValue(String enumValue) {
		this.enumValue = enumValue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getEnumId() {
		return enumId;
	}
	public void setEnumId(Integer enumId) {
		this.enumId = enumId;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

}
