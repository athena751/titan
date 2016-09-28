package com.hp.titan.test.vo;

import com.hp.app.vo.BaseVo;
import java.util.Date;

public class TestjobVo extends BaseVo{
	private String testjobID;
	private String testjobCode;
	private String testjobName;
	private Integer timeout;
	private String testjobType;
	private String testjobState;
	private String updateDate;
	private String sprintId;
	private String sprint;
	private String owner;
	private Integer ownerId;
	private String project;
	private String projectId;
	private String description;
	private String tag;
	
	
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
	public String getTestjobID() {
		return testjobID;
	}
	public void setTestjobID(String testjobID) {
		this.testjobID = testjobID;
	}
	public String getTestjobCode() {
		return testjobCode;
	}
	public void setTestjobCode(String testjobCode) {
		this.testjobCode = testjobCode;
	}
	public String getTestjobName() {
		return testjobName;
	}
	public void setTestjobName(String testjobName) {
		this.testjobName = testjobName;
	}
	public Integer getTimeout() {
		return timeout;
	}
	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}
	public String getTestjobState() {
		return testjobState;
	}
	public void setTestjobState(String testjobState) {
		this.testjobState = testjobState;
	}
	public String getSprintId() {
		return sprintId;
	}
	public void setSprintId(String sprintId) {
		this.sprintId = sprintId;
	}
	public String getSprint() {
		return sprint;
	}
	public void setSprint(String sprint) {
		this.sprint = sprint;
	}
	public String getTestjobType() {
		return testjobType;
	}
	public void setTestjobType(String testjobType) {
		this.testjobType = testjobType;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}

}
