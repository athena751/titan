package com.hp.titan.report.vo;
import java.util.Date;

import com.hp.app.vo.BaseVo;
public class DefectReportVo  extends BaseVo{

	private String userCode;
	private Integer usersubmitNum;
	private Integer userdeveloperNum;
	
	private Integer defaultCount;
	private Integer submittedCount;
	private Integer openCount;
	private Integer fixedCount;
	private Integer closedCount;
	
	private Integer lowCount;
	private Integer normalCount;
	private Integer highCount;
	private Integer resolveImmCount;
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public Integer getDefaultCount() {
		return defaultCount;
	}
	public void setDefaultCount(Integer defaultCount) {
		this.defaultCount = defaultCount;
	}
	public Integer getSubmittedCount() {
		return submittedCount;
	}
	public void setSubmittedCount(Integer submittedCount) {
		this.submittedCount = submittedCount;
	}
	public Integer getFixedCount() {
		return fixedCount;
	}
	public void setFixedCount(Integer fixedCount) {
		this.fixedCount = fixedCount;
	}
	public Integer getLowCount() {
		return lowCount;
	}
	public void setLowCount(Integer lowCount) {
		this.lowCount = lowCount;
	}
	public Integer getHighCount() {
		return highCount;
	}
	public void setHighCount(Integer highCount) {
		this.highCount = highCount;
	}
	public Integer getOpenCount() {
		return openCount;
	}
	public void setOpenCount(Integer openCount) {
		this.openCount = openCount;
	}
	public Integer getClosedCount() {
		return closedCount;
	}
	public void setClosedCount(Integer closedCount) {
		this.closedCount = closedCount;
	}
	public Integer getNormalCount() {
		return normalCount;
	}
	public void setNormalCount(Integer normalCount) {
		this.normalCount = normalCount;
	}
	public Integer getResolveImmCount() {
		return resolveImmCount;
	}
	public void setResolveImmCount(Integer resolveImmCount) {
		this.resolveImmCount = resolveImmCount;
	}
	public Integer getUsersubmitNum() {
		return usersubmitNum;
	}
	public void setUsersubmitNum(Integer usersubmitNum) {
		this.usersubmitNum = usersubmitNum;
	}
	public Integer getUserdeveloperNum() {
		return userdeveloperNum;
	}
	public void setUserdeveloperNum(Integer userdeveloperNum) {
		this.userdeveloperNum = userdeveloperNum;
	}
}
	
	