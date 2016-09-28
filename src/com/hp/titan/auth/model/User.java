package com.hp.titan.auth.model;
// default package
// Generated 2010-5-26 17:40:21 by Hibernate Tools 3.3.0.GA

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.app.model.BaseModel;
import com.hp.titan.project.model.Project;


/**
 * 用户
 */
public class User extends BaseModel implements java.io.Serializable, Comparable<Object>{

	private Integer userId;
	private String userCode;
	private String password;
	private String mail;
	private Integer confirmUserId;
	private Integer isValid=0;
	private String 	remark;
	private Integer groupId;
	private String roleId;
	private String  status;
	private String defaultprojectId;
	private Set<Role> userRoles = new HashSet<Role>(0);
	private Set<Group> userGroups = new HashSet<Group>(0);
	private Set<Project> userProjects = new HashSet<Project>(0);
	private List<Auth> authList =new ArrayList<Auth>();
    private Map<String, String> authMap = new HashMap<String, String>();
    private Date loginTime;
    
	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Map<String, String> getAuthMap() {
		return authMap;
	}

	public void setAuthMap(Map<String, String> authMap) {
		this.authMap = authMap;
	}

	public List<Auth> getAuthList() {
		return authList;
	}

	public void setAuthList(List<Auth> authList) {
		this.authList = authList;
	}

	public User() {}
	
	public Set<Group> getUserGroups() {
		return userGroups;
	}


	public void setUserGroups(Set<Group> userGroups) {
		this.userGroups = userGroups;
	}


	public Integer getGroupId() {
		return groupId;
	}


	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}



	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	 

	public String getMail() {
		return mail;
	}


	public void setMail(String mail) {
		this.mail = mail;
	}

	public Integer getUserId() {
		return userId;
	}


	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public String getUserCode() {
		return userCode;
	}





	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}





	public Integer getIsValid() {
		return isValid;
	}





	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}





	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}



	public Set<Role> getUserRoles() {
		return userRoles;
	}


	public void setUserRoles(Set<Role> userRoles) {
		this.userRoles = userRoles;
	}



	public int compareTo(Object obj) {
		int i =0;
		if (obj == null || !(obj instanceof User)) {
			System.out.println("空类或类型错误！");	
		}	
		return i;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Integer getConfirmUserId() {
		return confirmUserId;
	}

	public void setConfirmUserId(Integer confirmUserId) {
		this.confirmUserId = confirmUserId;
	}

	public Set<Project> getUserProjects() {
		return userProjects;
	}

	public void setUserProjects(Set<Project> userProjects) {
		this.userProjects = userProjects;
	}

	public String getDefaultprojectId() {
		return defaultprojectId;
	}

	public void setDefaultprojectId(String defaultprojectId) {
		this.defaultprojectId = defaultprojectId;
	}


}
