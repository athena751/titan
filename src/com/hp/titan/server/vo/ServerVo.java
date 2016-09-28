package com.hp.titan.server.vo;

import com.hp.app.vo.BaseVo;

public class ServerVo extends BaseVo {

	
	private String serverId;
	private String serverName;
	private Integer ownerId;
	private String owner;
	private String projectId;
	private String project;
	private Integer groupId;
	private String serverIp;
	private String consoleIp;
	private String serverStatus;
	private String serverType;
	private String memory;
	private String cpu;
	private String firmware;
	private String diskSize;
	private String distro;
	private String remark;
	private String location;
	private String sn;
	private String macAddr;
	private String resNote;
	private String hastab;
	
	private String purchaseDate;
	
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public Integer getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public String getConsoleIp() {
		return consoleIp;
	}
	public void setConsoleIp(String consoleIp) {
		this.consoleIp = consoleIp;
	}
	public String getServerStatus() {
		return serverStatus;
	}
	public void setServerStatus(String serverStatus) {
		this.serverStatus = serverStatus;
	}
	public String getServerType() {
		return serverType;
	}
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	public String getMemory() {
		return memory;
	}
	public void setMemory(String memory) {
		this.memory = memory;
	}
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public String getFirmware() {
		return firmware;
	}
	public void setFirmware(String firmware) {
		this.firmware = firmware;
	}
	public String getDiskSize() {
		return diskSize;
	}
	public void setDiskSize(String diskSize) {
		this.diskSize = diskSize;
	}
	public String getDistro() {
		return distro;
	}
	public void setDistro(String distro) {
		this.distro = distro;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getMacAddr() {
		return macAddr;
	}
	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}
	public String getResNote() {
		return resNote;
	}
	public void setResNote(String resNote) {
		this.resNote = resNote;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getPurchaseDate() {
		return purchaseDate;
	}
	public String getHastab() {
		return hastab;
	}
	public void setHastab(String hastab) {
		this.hastab = hastab;
	}
}
