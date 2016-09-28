package com.hp.titan.auth.model;
// default package
// Generated 2010-5-26 17:40:21 by Hibernate Tools 3.3.0.GA

/**
 * 用户角色关系
 */
public class UserRole implements java.io.Serializable {

	private UserRoleId id;
	private Role role;
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserRole() {
	}

	public UserRole(UserRoleId id, Role role) {
		this.id = id;
		this.role = role;
	}

	public UserRoleId getId() {
		return this.id;
	}

	public void setId(UserRoleId id) {
		this.id = id;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
