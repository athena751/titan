package com.hp.titan.test.model;

public class TestPlanCase {
	private TestPlanCaseId  id;
	private String sort;
	private Testcase testcase;
	
	public TestPlanCaseId getId() {
		return id;
	}
	public void setId(TestPlanCaseId id) {
		this.id = id;
	}
	public Testcase getTestcase() {
		return testcase;
	}
	public void setTestcase(Testcase testcase) {
		this.testcase = testcase;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	 
 
}
