package com.hp.titan.server.model;
import java.util.Date;

import com.hp.titan.auth.model.User;
import com.hp.titan.project.model.Project;

public class Storage  implements java.io.Serializable{

	private String storageId;
	private Integer ownerId;
	private Integer groupId;
	private String projectId;
	private String storageName;
	private String storageState;
	private String storageType;
	private String storageStatus;
	private String location;
	private String sn;	
	private String pn;
	private String capacity;
	private String slotNum;
	private String slotUsed;
	private Date purchaseDate;
	private String description;
	private String remark;
	private Integer createUserId;
	private Date createDate;
	private Integer updateUserId;
	private Date updateDate;
	private Integer isValid;
	private String str1;
	private String str2;
	private String str3;
	private String str4;
	private String str5;
	private String str6;
	private String str7;
	private Integer integer1;
	private Integer integer2;
	private Integer integer3;
	private User owner;
	private Project project;
	
	
	
	public String getStorageId() {
		return storageId;
	}
	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}
	public Integer getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
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
	public String getStorageName() {
		return storageName;
	}
	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}
	public String getStorageState() {
		return storageState;
	}
	public void setStorageState(String storageState) {
		this.storageState = storageState;
	}
	public String getStorageType() {
		return storageType;
	}
	public void setStorageType(String storageType) {
		this.storageType = storageType;
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
	public String getPn() {
		return pn;
	}
	public void setPn(String pn) {
		this.pn = pn;
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
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
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getStr1() {
		return str1;
	}
	public void setStr1(String str1) {
		this.str1 = str1;
	}
	public String getStr2() {
		return str2;
	}
	public void setStr2(String str2) {
		this.str2 = str2;
	}
	public String getStr3() {
		return str3;
	}
	public void setStr3(String str3) {
		this.str3 = str3;
	}
	public String getStr4() {
		return str4;
	}
	public void setStr4(String str4) {
		this.str4 = str4;
	}
	public String getStr5() {
		return str5;
	}
	public void setStr5(String str5) {
		this.str5 = str5;
	}
	public String getStr6() {
		return str6;
	}
	public void setStr6(String str6) {
		this.str6 = str6;
	}
	public String getStr7() {
		return str7;
	}
	public void setStr7(String str7) {
		this.str7 = str7;
	}
	public Integer getInteger1() {
		return integer1;
	}
	public void setInteger1(Integer integer1) {
		this.integer1 = integer1;
	}
	public Integer getInteger2() {
		return integer2;
	}
	public void setInteger2(Integer integer2) {
		this.integer2 = integer2;
	}
	public Integer getInteger3() {
		return integer3;
	}
	public void setInteger3(Integer integer3) {
		this.integer3 = integer3;
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
	public String getStorageStatus() {
		return storageStatus;
	}
	public void setStorageStatus(String storageStatus) {
		this.storageStatus = storageStatus;
	}
	
	
}
