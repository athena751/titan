package com.hp.titan.test.vo;

import com.hp.app.vo.BaseVo;

public class TestplanVo extends BaseVo{
	private String testplanID;
	private String testplanCode;
	private String testplanName;
	private String testplanType;
	private String owner;
	private Integer ownerId;
	private String project;
	private String projectId;
	private String description;
	
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}	
	public Integer getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getTestplanID() {
		return testplanID;
	}
	public void setTestplanID(String testplanID) {
		this.testplanID = testplanID;
	}
	public String getTestplanCode() {
		return testplanCode;
	}
	public void setTestplanCode(String testplanCode) {
		this.testplanCode = testplanCode;
	}
	public String getTestplanName() {
		return testplanName;
	}
	public void setTestplanName(String testplanName) {
		this.testplanName = testplanName;
	}
	public String getTestplanType() {
		return testplanType;
	}
	public void setTestplanType(String testplanType) {
		this.testplanType = testplanType;
	}
}
