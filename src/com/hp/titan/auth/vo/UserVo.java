package com.hp.titan.auth.vo;

import com.hp.app.vo.BaseVo;

public class UserVo extends BaseVo{
	private String userCode;
	private String roleId;
	private String confirmUserId;
	private String status;
	
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getConfirmUserId() {
		return confirmUserId;
	}

	public void setConfirmUserId(String confirmUserId) {
		this.confirmUserId = confirmUserId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
