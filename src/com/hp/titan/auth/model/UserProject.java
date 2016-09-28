package com.hp.titan.auth.model;

import com.hp.titan.project.model.Project;

public class UserProject {
	private UserProjectId  id;
	private Project project;
	
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public UserProjectId getId() {
		return id;
	}
	public void setId(UserProjectId id) {
		this.id = id;
	}
	 
 
}
