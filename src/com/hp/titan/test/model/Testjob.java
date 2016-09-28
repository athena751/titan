package com.hp.titan.test.model;

// Generated Dec 1, 2013 1:05:47 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.hp.titan.auth.model.User;
import com.hp.titan.project.model.Project;




public class Testjob implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	private String testjobId;
	private String testjobCode;
	private String testjobName;
	private String jobType;
	private Integer ownerId;
	private String projectId;
	private String sprintId;
	private Integer timeout;
	private String plancaseJson;
	private String state;	
	private String remark;
	private Integer createUserId;
	private Date createDate;
	private Integer updateUserId;
	private Date updateDate;
	private Integer isValid = 0;
	private String tag;
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
	
//	private Set<Testcase> testcases = new HashSet<Testcase>(0);
//	private Set<Testplan> testplans = new HashSet<Testplan>(0);
	
	public String getTestjobId() {
		return testjobId;
	}
	public void setTestjobId(String testjobId) {
		this.testjobId = testjobId;
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
	public String getSprintId() {
		return sprintId;
	}
	public void setSprintId(String sprintId) {
		this.sprintId = sprintId;
	}
	public Integer getTimeout() {
		return timeout;
	}
	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public Integer getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
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
//	public Set<Testcase> getTestcases() {
//		return testcases;
//	}
//	public void setTestcases(Set<Testcase> testcases) {
//		this.testcases = testcases;
//	}
//	public Set<Testplan> getTestplans() {
//		return testplans;
//	}
//	public void setTestplans(Set<Testplan> testplans) {
//		this.testplans = testplans;
//	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getPlancaseJson() {
		return plancaseJson;
	}
	public void setPlancaseJson(String plancaseJson) {
		this.plancaseJson = plancaseJson;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
}
