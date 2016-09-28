package com.hp.app.action;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.hp.titan.common.constants.TitanContent;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class DefaultBaseAction extends ActionSupport implements
		IPageControlEnable {
	
	protected Logger logger = Logger.getLogger(this.getClass());
	protected int pageNo = 1;
	protected int pageSize = 10;
	protected String t;
	protected String pageTable_length;

	protected String getWebApplicationAbsolutePath() {
		String realPath = getServletContext().getRealPath("/");
		if ((realPath!= null) && (!realPath.endsWith("/"))) {
			realPath = realPath + "/";
		}
		return realPath;
	}
	
	public Map<String, String> getLocalMap() {
		if(TitanContent.LOCALE_CH.equals(this.getHttpSession().getAttribute(TitanContent.LOCALE_LANGUAGE).toString())) {
			return TitanContent.map_ch;
		} else {
			return TitanContent.map_en;
		}	
	}

	public int getPageNo() {
		return this.pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	protected ServletContext getServletContext() {
		return ServletActionContext.getServletContext();
	}

	protected HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	protected HttpSession getHttpSession() {
		return ServletActionContext.getRequest().getSession();
	}

	protected HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

	public String getPageTable_length() {
		return pageTable_length;
	}

	public void setPageTable_length(String pageTableLength) {
		pageTable_length = pageTableLength;
	}
}
