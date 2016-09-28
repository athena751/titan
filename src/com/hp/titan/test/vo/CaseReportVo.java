package com.hp.titan.test.vo;

import com.hp.app.vo.BaseVo;

public class CaseReportVo extends BaseVo{
	
	private String caseID;
	private String caseCode;
	private String caseName;
	private Integer caseRunTotal;
	private Integer caseRunPass;
	private Integer caseRunFail;
	private Integer caseRunSkip;
	private Integer caseRunNotrun;
	private Integer caseRunRuning;
	private String casepassCent;
	private String casefailCent;
	private String casenotrunCent;
	
	
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
	public String getCaseID() {
		return caseID;
	}
	public void setCaseID(String caseID) {
		this.caseID = caseID;
	}
	public String getCaseCode() {
		return caseCode;
	}
	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}
	public String getCaseName() {
		return caseName;
	}
	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}
	public Integer getCaseRunTotal() {
		return caseRunTotal;
	}
	public void setCaseRunTotal(Integer caseRunTotal) {
		this.caseRunTotal = caseRunTotal;
	}
	public Integer getCaseRunPass() {
		return caseRunPass;
	}
	public void setCaseRunPass(Integer caseRunPass) {
		this.caseRunPass = caseRunPass;
	}
	public Integer getCaseRunFail() {
		return caseRunFail;
	}
	public void setCaseRunFail(Integer caseRunFail) {
		this.caseRunFail = caseRunFail;
	}
	public Integer getCaseRunSkip() {
		return caseRunSkip;
	}
	public void setCaseRunSkip(Integer caseRunSkip) {
		this.caseRunSkip = caseRunSkip;
	}
	public Integer getCaseRunNotrun() {
		return caseRunNotrun;
	}
	public void setCaseRunNotrun(Integer caseRunNotrun) {
		this.caseRunNotrun = caseRunNotrun;
	}
	public Integer getCaseRunRuning() {
		return caseRunRuning;
	}
	public void setCaseRunRuning(Integer caseRunRuning) {
		this.caseRunRuning = caseRunRuning;
	}
	public String getCasenotrunCent() {
		return casenotrunCent;
	}
	public void setCasenotrunCent(String casenotrunCent) {
		this.casenotrunCent = casenotrunCent;
	}

}
