package com.hp.titan.test.model;

// Generated Dec 1, 2013 1:05:47 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.hp.titan.auth.model.User;





public class RunJob implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	private String runJobId;
	private String jobId;
	private Date startTime;
	private Date endTime;
	private Integer isDone;	
	private String result;
	private String remark;
	private Integer createUserId;
	private Date createDate;
	private Integer updateUserId;
	private Date updateDate;
	private Integer isValid;
	private String str1;
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

	
	public String getRunJobId() {
		return runJobId;
	}
	public void setRunJobId(String runJobId) {
		this.runJobId = runJobId;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getIsDone() {
		return isDone;
	}
	public void setIsDone(Integer isDone) {
		this.isDone = isDone;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
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
	public String getStr1() {
		return str1;
	}
	public void setStr1(String str1) {
		this.str1 = str1;
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

}
