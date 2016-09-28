package com.hp.titan.project.vo;

import com.hp.app.vo.BaseVo;

public class ReportVo extends BaseVo{
	
	private Integer testjobTotal;
	private Integer testjobPass;
	private Integer testjobFail;
	private Integer testjobActive;
	private Integer testjobPending;
	private Integer testjobRunning;
	private Integer testjobAbort;
	private Integer testcaseTotal;
	private Integer testcasePass;
	private Integer testcaseFail;
	private Integer testcaseSkip;
	private Integer testcaseNotrun;
	private Integer testcaseRuning;
	private Integer hasDefect;
	private Integer noDefect;
	private Integer defectFixed;
	private Integer defectNotfixed;
	
	private String passCent;
	private String failCent;
	private String activeCent;
	private String pendCent;
	private String runCent;
	private String abortCent;
	private String sprintName;
	
	private String casepassCent;
	private String casefailCent;
	private String casenotrunCent;
	private String caseskipCent;
	private String caserunCent;
	
	private String hasdefectCent;
	private String nodefectCent;
	private String defectfixedCent;
	private String defectnotfixedCent;
	
	
	public Integer getTestcaseSkip() {
		return testcaseSkip;
	}
	public void setTestcaseSkip(Integer testcaseSkip) {
		this.testcaseSkip = testcaseSkip;
	}
	public Integer getTestcaseNotrun() {
		return testcaseNotrun;
	}
	public void setTestcaseNotrun(Integer testcaseNotrun) {
		this.testcaseNotrun = testcaseNotrun;
	}
	public Integer getTestcaseRuning() {
		return testcaseRuning;
	}
	public void setTestcaseRuning(Integer testcaseRuning) {
		this.testcaseRuning = testcaseRuning;
	}
	public String getCasenotrunCent() {
		return casenotrunCent;
	}
	public void setCasenotrunCent(String casenotrunCent) {
		this.casenotrunCent = casenotrunCent;
	}
	public String getCaseskipCent() {
		return caseskipCent;
	}
	public void setCaseskipCent(String caseskipCent) {
		this.caseskipCent = caseskipCent;
	}
	public String getCaserunCent() {
		return caserunCent;
	}
	public void setCaserunCent(String caserunCent) {
		this.caserunCent = caserunCent;
	}
	public Integer getTestjobActive() {
		return testjobActive;
	}
	public void setTestjobActive(Integer testjobActive) {
		this.testjobActive = testjobActive;
	}
	public Integer getTestjobPending() {
		return testjobPending;
	}
	public void setTestjobPending(Integer testjobPending) {
		this.testjobPending = testjobPending;
	}
	public Integer getTestjobRunning() {
		return testjobRunning;
	}
	public void setTestjobRunning(Integer testjobRunning) {
		this.testjobRunning = testjobRunning;
	}
	public String getActiveCent() {
		return activeCent;
	}
	public void setActiveCent(String activeCent) {
		this.activeCent = activeCent;
	}
	public String getPendCent() {
		return pendCent;
	}
	public void setPendCent(String pendCent) {
		this.pendCent = pendCent;
	}
	public String getRunCent() {
		return runCent;
	}
	public void setRunCent(String runCent) {
		this.runCent = runCent;
	}

	public Integer getTestjobTotal() {
		return testjobTotal;
	}
	public void setTestjobTotal(Integer testjobTotal) {
		this.testjobTotal = testjobTotal;
	}
	public Integer getTestjobPass() {
		return testjobPass;
	}
	public void setTestjobPass(Integer testjobPass) {
		this.testjobPass = testjobPass;
	}
	public Integer getTestjobFail() {
		return testjobFail;
	}
	public void setTestjobFail(Integer testjobFail) {
		this.testjobFail = testjobFail;
	}
	public String getPassCent() {
		return passCent;
	}
	public void setPassCent(String passCent) {
		this.passCent = passCent;
	}
	public String getFailCent() {
		return failCent;
	}
	public void setFailCent(String failCent) {
		this.failCent = failCent;
	}
	public Integer getTestcaseTotal() {
		return testcaseTotal;
	}
	public void setTestcaseTotal(Integer testcaseTotal) {
		this.testcaseTotal = testcaseTotal;
	}
	public Integer getTestcasePass() {
		return testcasePass;
	}
	public void setTestcasePass(Integer testcasePass) {
		this.testcasePass = testcasePass;
	}
	public Integer getTestcaseFail() {
		return testcaseFail;
	}
	public void setTestcaseFail(Integer testcaseFail) {
		this.testcaseFail = testcaseFail;
	}
	public String getCasepassCent() {
		return casepassCent;
	}
	public void setCasepassCent(String casepassCent) {
		this.casepassCent = casepassCent;
	}
	public String getCasefailCent() {
		return casefailCent;
	}
	public void setCasefailCent(String casefailCent) {
		this.casefailCent = casefailCent;
	}
	public String getSprintName() {
		return sprintName;
	}
	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}
	public String getAbortCent() {
		return abortCent;
	}
	public void setAbortCent(String abortCent) {
		this.abortCent = abortCent;
	}
	public Integer getTestjobAbort() {
		return testjobAbort;
	}
	public void setTestjobAbort(Integer testjobAbort) {
		this.testjobAbort = testjobAbort;
	}
	public Integer getHasDefect() {
		return hasDefect;
	}
	public void setHasDefect(Integer hasDefect) {
		this.hasDefect = hasDefect;
	}
	public Integer getNoDefect() {
		return noDefect;
	}
	public void setNoDefect(Integer noDefect) {
		this.noDefect = noDefect;
	}
	public Integer getDefectFixed() {
		return defectFixed;
	}
	public void setDefectFixed(Integer defectFixed) {
		this.defectFixed = defectFixed;
	}
	public Integer getDefectNotfixed() {
		return defectNotfixed;
	}
	public void setDefectNotfixed(Integer defectNotfixed) {
		this.defectNotfixed = defectNotfixed;
	}
	public String getHasdefectCent() {
		return hasdefectCent;
	}
	public void setHasdefectCent(String hasdefectCent) {
		this.hasdefectCent = hasdefectCent;
	}
	public String getNodefectCent() {
		return nodefectCent;
	}
	public void setNodefectCent(String nodefectCent) {
		this.nodefectCent = nodefectCent;
	}
	public String getDefectfixedCent() {
		return defectfixedCent;
	}
	public void setDefectfixedCent(String defectfixedCent) {
		this.defectfixedCent = defectfixedCent;
	}
	public String getDefectnotfixedCent() {
		return defectnotfixedCent;
	}
	public void setDefectnotfixedCent(String defectnotfixedCent) {
		this.defectnotfixedCent = defectnotfixedCent;
	}
}
