package com.hp.titan.test.vo;

import java.util.Date;

import com.hp.app.vo.BaseVo;

public class TestcaserunVo extends BaseVo{
	private String testcaseId;
	private String testplanId;
	private String testcaseCode;
	private String testcaseName;
	private String testcaseState;
	private String runtestcaseId;
	private String state;
	private String command;
	public String getTestcaseId() {
		return testcaseId;
	}
	public void setTestcaseId(String testcaseId) {
		this.testcaseId = testcaseId;
	}
	public String getTestcaseCode() {
		return testcaseCode;
	}
	public void setTestcaseCode(String testcaseCode) {
		this.testcaseCode = testcaseCode;
	}
	public String getTestcaseName() {
		return testcaseName;
	}
	public void setTestcaseName(String testcaseName) {
		this.testcaseName = testcaseName;
	}
	public String getTestcaseState() {
		return testcaseState;
	}
	public void setTestcaseState(String testcaseState) {
		this.testcaseState = testcaseState;
	}
	public String getRuntestcaseId() {
		return runtestcaseId;
	}
	public void setRuntestcaseId(String runtestcaseId) {
		this.runtestcaseId = runtestcaseId;
	}
	public String getTestplanId() {
		return testplanId;
	}
	public void setTestplanId(String testplanId) {
		this.testplanId = testplanId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
}
