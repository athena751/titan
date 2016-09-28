package com.hp.titan.server.vo;

import com.hp.app.vo.BaseVo;

public class CardVo extends BaseVo {

	
	private String cardId;
	private String serverId;
	private String serverName;
	private String cardName;
	private String cardType;
	private String cardSn;
	private String cardPn;
	private String vendor;
	private String wwn;
	private String projectId;
	private Integer groupId;
	private String project;
	private String location;
	private String description;
	private String cardState;
	private Integer ownerId;
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
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardSn() {
		return cardSn;
	}
	public void setCardSn(String cardSn) {
		this.cardSn = cardSn;
	}
	public String getCardPn() {
		return cardPn;
	}
	public void setCardPn(String cardPn) {
		this.cardPn = cardPn;
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
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public String getCardState() {
		return cardState;
	}
	public void setCardState(String cardState) {
		this.cardState = cardState;
	}
	public Integer getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	
}
