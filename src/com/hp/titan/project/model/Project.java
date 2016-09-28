package com.hp.titan.project.model;

import com.hp.app.model.BaseModel;
import com.hp.titan.auth.model.Group;
import com.hp.titan.auth.model.User;

public class Project extends BaseModel implements java.io.Serializable {

	private String projectId;
	private String projectName;
	private int pmId;
	private int groupId;
	private String remark;
	private Integer isValid=0;
	private User projectManger;
	private Group group;
	
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public int getPmId() {
		return pmId;
	}
	public void setPmId(int pmId) {
		this.pmId = pmId;
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
	public User getProjectManger() {
		return projectManger;
	}
	public void setProjectManger(User projectManger) {
		this.projectManger = projectManger;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}

}
