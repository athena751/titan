package com.hp.titan.test.model;
// default package

/**
 * UserTagId generated by hbm2java
 */
public class UserTagId implements java.io.Serializable {


	private Integer userId;
	private String tag;
	public UserTagId() {
	}

	public UserTagId(String testcaseId, String testjobId) {
		this.userId = userId;
		this.tag = tag;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}


}
