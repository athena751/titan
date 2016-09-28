package com.hp.titan.sprint.model;


import java.sql.Timestamp;
import java.util.Date;

import com.hp.app.model.BaseModel;
import com.hp.titan.project.model.Project;

public class Sprint extends BaseModel implements java.io.Serializable {

	private SprintId Id;
	private String sprintName;
	private String remark;
	private String state;
	private Date startDate;
	private Date endDate;
	
	private Integer isValid=0;
	
	private Project project;
	
	
	public String getSprintName() {
		return sprintName;
	}
	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getIsValid() {
		return isValid;
	}
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public SprintId getId() {
		return Id;
	}
	public void setId(SprintId id) {
		Id = id;
	}

}
