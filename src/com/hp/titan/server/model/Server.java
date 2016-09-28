package com.hp.titan.server.model;
import java.util.Date;

import com.hp.titan.auth.model.User;
import com.hp.titan.project.model.Project;

public class Server  implements java.io.Serializable{

	private String serverId;
	private Integer ownerId;
	private String serverName;
	private String serverIp;
	private String serverAccount;
	private String serverPasswd;
	private String consoleIp;
	private String consoleAccount;
	private String consolePasswd;
	private Integer groupId;
	private String projectId;
	private String serverState;
	private String serverType;
	private Date purchaseDate;
	private String location;
	private String macAddr;
	private String sn;
	private String pn;
	private String memory;
	private String cpu;
	private String firmware;
	private String diskSize;
	private String distro;
	private String description;
	private String remark;
	private Integer createUserId;
	private Date createDate;
	private Integer updateUserId;
	private Date updateDate;
	private Integer keyServer;
	private String status;
	private String hastab;
	
	public Integer getKeyServer() {
		return keyServer;
	}
	public void setKeyServer(Integer keyServer) {
		this.keyServer = keyServer;
	}
	private Integer isValid;
	
	private User owner;
	private Project project;	
	
	
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public Integer getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public String getServerAccount() {
		return serverAccount;
	}
	public void setServerAccount(String serverAccount) {
		this.serverAccount = serverAccount;
	}
	public String getServerPasswd() {
		return serverPasswd;
	}
	public void setServerPasswd(String serverPasswd) {
		this.serverPasswd = serverPasswd;
	}
	public String getConsoleIp() {
		return consoleIp;
	}
	public void setConsoleIp(String consoleIp) {
		this.consoleIp = consoleIp;
	}
	
	public String getConsoleAccount() {
		return consoleAccount;
	}
	public void setConsoleAccount(String consoleAccount) {
		this.consoleAccount = consoleAccount;
	}
	public String getConsolePasswd() {
		return consolePasswd;
	}
	public void setConsolePasswd(String consolePasswd) {
		this.consolePasswd = consolePasswd;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getServerState() {
		return serverState;
	}
	public void setServerState(String serverState) {
		this.serverState = serverState;
	}
	public String getServerType() {
		return serverType;
	}
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getMacAddr() {
		return macAddr;
	}
	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
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
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Integer getIsValid() {
		return isValid;
	}
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPn() {
		return pn;
	}
	public void setPn(String pn) {
		this.pn = pn;
	}
	public String getHastab() {
		return hastab;
	}
	public void setHastab(String hastab) {
		this.hastab = hastab;
	}

}
