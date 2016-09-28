package com.hp.titan.enumtype.model;
import java.util.Date;



public class EnumType  implements java.io.Serializable{

	private Integer typeId;
	private String typeName;
	private String description;

	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	
}
