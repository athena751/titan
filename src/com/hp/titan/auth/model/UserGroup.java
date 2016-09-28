package com.hp.titan.auth.model;

public class UserGroup {
	private UserGroupId  id;
	private Group group;
	
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public UserGroupId getId() {
		return id;
	}
	public void setId(UserGroupId id) {
		this.id = id;
	}
	 
 
}
