package com.hp.titan.test.model;
// default package
// Generated 2010-5-26 17:40:21 by Hibernate Tools 3.3.0.GA

/**
 * UserRoleId generated by hbm2java
 */
public class TestPlanCaseId implements java.io.Serializable {


	private String testcaseId;
	private String testplanId;
	public TestPlanCaseId() {
	}

	public TestPlanCaseId(String testcaseId, String testplanId) {
		this.testcaseId = testcaseId;
		this.testplanId = testplanId;
	}

	public String getTestcaseId() {
		return testcaseId;
	}

	public void setTestcaseId(String testcaseId) {
		this.testcaseId = testcaseId;
	}

	public String getTestplanId() {
		return testplanId;
	}

	public void setTestplanId(String testplanId) {
		this.testplanId = testplanId;
	}


}