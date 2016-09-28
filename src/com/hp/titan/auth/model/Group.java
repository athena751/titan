package com.hp.titan.auth.model;

// default package
// Generated 2010-5-26 17:40:21 by Hibernate Tools 3.3.0.GA

import java.util.HashSet;
import java.util.Set;

import com.hp.app.model.BaseModel;

/**
 * 用户组
 */
public class Group extends BaseModel implements java.io.Serializable {

	private int     groupId;  
	private String  groupName;
	private String descritption;   
	private Integer isValid=0; 
	private Integer level;    
	private String remark;    
	private String supperGroupId; 
	private String supperGroupName; 
	private Set<User> users= new HashSet<User>(0); 

	public String getDescritption() {
		return descritption;
	}



	public void setDescritption(String descritption) {
		this.descritption = descritption;
	}

	public Set<User> getUsers() {
		return users;
	}



	public void setUsers(Set<User> users) {
		this.users = users;
	}



	public int getGroupId() {
		return groupId;
	}



	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}



	public String getGroupName() {
		return groupName;
	}



	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}



	public Integer getIsValid() {
		return isValid;
	}



	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}



	public Integer getLevel() {
		return level;
	}



	public void setLevel(Integer level) {
		this.level = level;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public String getSupperGroupId() {
		return supperGroupId;
	}



	public void setSupperGroupId(String supperGroupId) {
		this.supperGroupId = supperGroupId;
	}



	public String getSupperGroupName() {
		return supperGroupName;
	}



	public void setSupperGroupName(String supperGroupName) {
		this.supperGroupName = supperGroupName;
	}



	public Group() {
	}

	   
 

}
