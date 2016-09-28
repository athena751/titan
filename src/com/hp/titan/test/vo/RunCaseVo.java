package com.hp.titan.test.vo;

import com.hp.app.vo.BaseVo;

public class RunCaseVo extends BaseVo{
	private String caseId;
	private String runcaseId;
	private String runjobId;
	private String planId;
	private String startTime;
	private String endTime;
	private String runUnser;
	private String state;
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getRunjobId() {
		return runjobId;
	}
	public void setRunjobId(String runjobId) {
		this.runjobId = runjobId;
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
	public String getRunUnser() {
		return runUnser;
	}
	public void setRunUnser(String runUnser) {
		this.runUnser = runUnser;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getRuncaseId() {
		return runcaseId;
	}
	public void setRuncaseId(String runcaseId) {
		this.runcaseId = runcaseId;
	}
	
}
