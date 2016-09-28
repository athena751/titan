package com.hp.titan.server.vo;

import com.hp.app.vo.BaseVo;

public class StorageVo extends BaseVo {

	
	private String storageId;
	private String storageName;
	private String storageType;
	private String capacity;
	private String sn;	
	private String pn;
	private String slotNum;
	private String slotUsed;
	private String description;
	private String projectId;
	private Integer groupId;
	private String project;
	
	
	public String getStorageId() {
		return storageId;
	}
	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}
	public String getStorageName() {
		return storageName;
	}
	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}
	public String getStorageType() {
		return storageType;
	}
	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getPn() {
		return pn;
	}
	public void setPn(String pn) {
		this.pn = pn;
	}
	public String getSlotNum() {
		return slotNum;
	}
	public void setSlotNum(String slotNum) {
		this.slotNum = slotNum;
	}
	public String getSlotUsed() {
		return slotUsed;
	}
	public void setSlotUsed(String slotUsed) {
		this.slotUsed = slotUsed;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	
}
