package com.hp.titan.auth.model;

import java.util.HashSet;
import java.util.Set;

import com.hp.app.model.BaseModel;


/**
 * 角色 
 */
public class Role extends BaseModel implements java.io.Serializable {

	private String roleId;
	private String roleName;
	private Integer roleState;
	private String roleDesc;
	private String remark;
	private Integer isValid=0;
	
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	private Set<Auth> auths = new HashSet<Auth>(0);
	private Set<User> userRoles = new HashSet<User>(0);
 
	
	public Role() {
	}

	public Role(String roleId) {
		this.roleId = roleId;
	}
	public Role(String roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}

	public Role(String roleId, String roleName, Integer roleState, String roleDesc, Set<Auth> auths,
			Set<User> userRoles) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleState = roleState;
		this.roleDesc = roleDesc;
		this.auths = auths;
		this.userRoles = userRoles;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getRoleState() {
		return this.roleState;
	}

	public void setRoleState(Integer roleState) {
		this.roleState = roleState;
	}

	public String getRoleDesc() {
		return this.roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public Set<Auth> getAuths() {
		return this.auths;
	}

	public void setAuths(Set<Auth> auths) {
		this.auths = auths;
	}

	public Set<User> getUserRoles() {
		return this.userRoles;
	}

	public void setUserRoles(Set<User> userRoles) {
		this.userRoles = userRoles;
	}

}
