package com.hp.titan.auth.model;

import java.util.HashSet;
import java.util.Set;

/**
 * 角色 
 */
public class RoleAuth implements java.io.Serializable {

	private RoleAuthId  id;
	private Auth auth;
	
	public RoleAuth() {
	}

	 
 
	public RoleAuthId getId() {
		return id;
	}



	public void setId(RoleAuthId id) {
		this.id = id;
	}



	public Auth getAuth() {
		return auth;
	}

	public void setAuth(Auth auth) {
		this.auth = auth;
	}
 

}
