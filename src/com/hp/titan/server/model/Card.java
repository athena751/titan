package com.hp.titan.server.model;
import java.util.Date;

import com.hp.app.model.BaseModel;
import com.hp.titan.project.model.Project;



public class Card implements java.io.Serializable{

	private String cardId;
	private String serverId;
	private String cardName;
	private String codeName;
	private Integer ownerId;
	private Integer groupId;
	private String projectId;
	private String cardType;
	private String cardStatus;
	private String sn;	
	private String pn;
	private String vendor;
	private String wwn;	
	private Date purchaseDate;
	private String location;
	private String description;
	private String remark;
	private Integer createUserId;
	private Date createDate;
	private Integer updateUserId;
	private Date updateDate;
	private Integer isValid;
	private String str4;
	private String str5;
	private String str6;
	private String str7;
	private Integer integer1;
	private Integer integer2;
	private Integer integer3;
	
	private Project project;
	private String cardState;
	
	
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
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
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
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
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getWwn() {
		return wwn;
	}
	public void setWwn(String wwn) {
		this.wwn = wwn;
	}
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getCardStatus() {
		return cardStatus;
	}
	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}
	public String getCardState() {
		return cardState;
	}
	public void setCardState(String cardState) {
		this.cardState = cardState;
	}

	

}
