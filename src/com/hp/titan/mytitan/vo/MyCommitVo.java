package com.hp.titan.mytitan.vo;

// Generated Dec 1, 2013 1:05:47 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * CaseLog generated by hbm2java
 */
public class MyCommitVo implements java.io.Serializable {
	
	private String commitreportId;
	private String reportaryId;
	private String projectName;
	private String revision;
	private String committedBy;
	private String commitTime;
	private String codeChange;
	private String comment;
	public String getCommitreportId() {
		return commitreportId;
	}
	public void setCommitreportId(String commitreportId) {
		this.commitreportId = commitreportId;
	}
	public String getReportaryId() {
		return reportaryId;
	}
	public void setReportaryId(String reportaryId) {
		this.reportaryId = reportaryId;
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	public String getCommittedBy() {
		return committedBy;
	}
	public void setCommittedBy(String committedBy) {
		this.committedBy = committedBy;
	}
	public String getCodeChange() {
		return codeChange;
	}
	public void setCodeChange(String codeChange) {
		this.codeChange = codeChange;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getCommitTime() {
		return commitTime;
	}
	public void setCommitTime(String commitTime) {
		this.commitTime = commitTime;
	}
}
