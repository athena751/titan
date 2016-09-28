package com.hp.titan.server.vo;

import com.hp.app.vo.BaseVo;

public class ResReportVo extends BaseVo {

	private Integer reserveNum;
	private String userCode;
	private String servers;
	private String projectName;
	private Integer rePorjNum;
	public Integer getReserveNum() {
		return reserveNum;
	}
	public void setReserveNum(Integer reserveNum) {
		this.reserveNum = reserveNum;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getServers() {
		return servers;
	}
	public void setServers(String servers) {
		this.servers = servers;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public Integer getRePorjNum() {
		return rePorjNum;
	}
	public void setRePorjNum(Integer rePorjNum) {
		this.rePorjNum = rePorjNum;
	}
	
}
