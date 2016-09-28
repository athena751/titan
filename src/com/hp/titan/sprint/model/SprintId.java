package com.hp.titan.sprint.model;


import java.sql.Timestamp;
import java.util.Date;

import com.hp.app.model.BaseModel;
import com.hp.titan.project.model.Project;

public class SprintId implements java.io.Serializable {

	private String sprintId;
	private String projectId;
	public String getSprintId() {
		return sprintId;
	}
	public void setSprintId(String sprintId) {
		this.sprintId = sprintId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
