package com.hp.titan.common.vo;

// Generated Dec 1, 2013 1:05:47 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * CaseLog generated by hbm2java
 */
public class UserstoryVo implements java.io.Serializable {
	
	private String objectId;
	private String usNum;
	private String usName;
	private String state;
	private String planEstimate;
	private String taskActuals;
	private String taskEstimates;
	private String taskTodos;
	private String ownerEmail;
	private String ownerObj;
	private String projectinRally;
	private String parentNum;
	private String acceptedDate;
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getUsNum() {
		return usNum;
	}
	public void setUsNum(String usNum) {
		this.usNum = usNum;
	}
	public String getUsName() {
		return usName;
	}
	public void setUsName(String usName) {
		this.usName = usName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getOwnerEmail() {
		return ownerEmail;
	}
	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}
	public String getProjectinRally() {
		return projectinRally;
	}
	public void setProjectinRally(String projectinRally) {
		this.projectinRally = projectinRally;
	}
	public String getPlanEstimate() {
		return planEstimate;
	}
	public void setPlanEstimate(String planEstimate) {
		this.planEstimate = planEstimate;
	}
	public String getTaskActuals() {
		return taskActuals;
	}
	public void setTaskActuals(String taskActuals) {
		this.taskActuals = taskActuals;
	}
	public String getTaskEstimates() {
		return taskEstimates;
	}
	public void setTaskEstimates(String taskEstimates) {
		this.taskEstimates = taskEstimates;
	}
	public String getTaskTodos() {
		return taskTodos;
	}
	public void setTaskTodos(String taskTodos) {
		this.taskTodos = taskTodos;
	}
	public String getOwnerObj() {
		return ownerObj;
	}
	public void setOwnerObj(String ownerObj) {
		this.ownerObj = ownerObj;
	}
	public String getParentNum() {
		return parentNum;
	}
	public void setParentNum(String parentNum) {
		this.parentNum = parentNum;
	}
	public String getAcceptedDate() {
		return acceptedDate;
	}
	public void setAcceptedDate(String acceptedDate) {
		this.acceptedDate = acceptedDate;
	}

}
