package com.hp.titan.common.util;

import org.apache.struts2.ServletActionContext;
import org.springframework.security.context.SecurityContextHolder;

import com.hp.titan.auth.model.User;
import com.hp.titan.mytitan.model.Point;

public final class UserSessionUtil {
	
	public final static String USER_CONTAINER = "loginUser";
	public final static String DEFAULT_PROJECT = "defaultProject";
	public final static String DEFAULT_PROJECT_NAME = "defaultProjectName";
	public final static String USER_POINT = "point";
	public final static String USER_EXP = "exp";
	public final static String USER_INBOX = "inbox";
	
	public static void setInbox(Integer inbox) {
		ServletActionContext.getRequest().getSession(true).setAttribute(USER_INBOX, inbox);
	}
	
	public static Integer getInbox() {
		if ( ServletActionContext.getRequest() != null ){
			return (Integer) ServletActionContext.getRequest().getSession().getAttribute(USER_INBOX);
		}
		return null;
	}
	public static void setExp(Integer exp) {
		ServletActionContext.getRequest().getSession(true).setAttribute(USER_EXP, exp);
	}
	
	public static Integer getExp() {
		if ( ServletActionContext.getRequest() != null ){
			return (Integer) ServletActionContext.getRequest().getSession().getAttribute(USER_EXP);
		}
		return null;
	}
	public static void setPoint(Integer point) {
		ServletActionContext.getRequest().getSession(true).setAttribute(USER_POINT, point);
	}	
	public static Integer getPoint() {
		if ( ServletActionContext.getRequest() != null ){
			return (Integer) ServletActionContext.getRequest().getSession().getAttribute(USER_POINT);
		}
		return null;
	}
	
	public static void setUser(User user) {
		ServletActionContext.getRequest().getSession(true).setAttribute(USER_CONTAINER, user);
	}

	public static User getUser() {
		if ( ServletActionContext.getRequest() != null ){
			return (User) ServletActionContext.getRequest().getSession().getAttribute(USER_CONTAINER);
		}
		return null;
	}
	
	public static void setDefaultProject(String projectId) {
		ServletActionContext.getRequest().getSession(true).setAttribute(DEFAULT_PROJECT, projectId);
	}
	
	public static String getDefaultProject(){
		if ( ServletActionContext.getRequest() != null ){
			return (String)ServletActionContext.getRequest().getSession().getAttribute(DEFAULT_PROJECT);
		}
		return null;
	}
	
	public static void setDefaultProjectName(String projectName) {
		ServletActionContext.getRequest().getSession(true).setAttribute(DEFAULT_PROJECT_NAME, projectName);
	}
	
	public static String getDefaultProjectName(){
		if ( ServletActionContext.getRequest() != null ){
			return (String)ServletActionContext.getRequest().getSession().getAttribute(DEFAULT_PROJECT_NAME);
		}
		return null;
	}
	
	public static void clearSession() {
		ServletActionContext.getRequest().getSession().removeAttribute(USER_CONTAINER);
	}
}
