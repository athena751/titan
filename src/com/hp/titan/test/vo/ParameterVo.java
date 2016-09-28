package com.hp.titan.test.vo;

import com.hp.app.vo.BaseVo;

public class ParameterVo extends BaseVo{
	private String paraName;
	private String paraValue;
	private String runcaseparaValue;
	public String getParaName() {
		return paraName;
	}
	public void setParaName(String paraName) {
		this.paraName = paraName;
	}
	public String getParaValue() {
		return paraValue;
	}
	public void setParaValue(String paraValue) {
		this.paraValue = paraValue;
	}
	public String getRuncaseparaValue() {
		return runcaseparaValue;
	}
	public void setRuncaseparaValue(String runcaseparaValue) {
		this.runcaseparaValue = runcaseparaValue;
	}
}
