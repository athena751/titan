package com.hp.titan.test.vo;

import java.util.Date;

import com.hp.app.vo.BaseVo;

public class TestjobrunVo extends BaseVo{
	private String runtestjobId;
	private String testjobId;
	private String testjobCode;
	private String testjobName;
	private String testjobType;
	private String testjobState;
	private Integer isdone;
	private Integer successCount;
	private Integer failCount;
	private String startTime;
	private String endTime;
	private String result;
	private String owner;
	public String getRuntestjobId() {
		return runtestjobId;
	}
	public void setRuntestjobId(String runtestjobId) {
		this.runtestjobId = runtestjobId;
	}
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
	public String getTestjobType() {
		return testjobType;
	}
	public void setTestjobType(String testjobType) {
		this.testjobType = testjobType;
	}
	public String getTestjobState() {
		return testjobState;
	}
	public void setTestjobState(String testjobState) {
		this.testjobState = testjobState;
	}
	public Integer getIsdone() {
		return isdone;
	}
	public void setIsdone(Integer isdone) {
		this.isdone = isdone;
	}
	public Integer getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}
	public Integer getFailCount() {
		return failCount;
	}
	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
}
