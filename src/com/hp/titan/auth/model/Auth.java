package com.hp.titan.auth.model;

// default package
// Generated 2010-5-26 17:40:21 by Hibernate Tools 3.3.0.GA

import java.util.HashSet;
import java.util.Set;

import com.hp.app.model.BaseModel;

/**
 * 权限表
 */
public class Auth extends BaseModel implements java.io.Serializable {

	
	private String authId;
	private String authName;
	private String authDesc;
	private String remark;
	private Integer isValid=0;
	private Integer menuId;
	private Set<Role> roles = new HashSet<Role>(0);
	private Menu menu;
	private boolean ischecked=false;
	public boolean isIschecked() {
		return ischecked;
	}

	public void setIschecked(boolean ischecked) {
		this.ischecked = ischecked;
	}

	public Auth() {
	}

	public Auth(String authId, String authName) {
		this.authId = authId;
		this.authName = authName;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	public String getAuthId() {
		return this.authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getAuthName() {
		return this.authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	 

	public String getAuthDesc() {
		return this.authDesc;
	}

	public void setAuthDesc(String authDesc) {
		this.authDesc = authDesc;
	}

	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

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

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

}
