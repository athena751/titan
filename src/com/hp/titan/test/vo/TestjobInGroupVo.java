package com.hp.titan.test.vo;

import java.util.List;

import com.hp.app.vo.BaseVo;

public class TestjobInGroupVo extends BaseVo{
	private List<TestjobVo> testjobVoList;
	private String tag;

	public List<TestjobVo> getTestjobVoList() {
		return testjobVoList;
	}

	public void setTestjobVoList(List<TestjobVo> testjobVoList) {
		this.testjobVoList = testjobVoList;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
