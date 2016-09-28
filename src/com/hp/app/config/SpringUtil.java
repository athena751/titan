package com.hp.app.config;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringUtil {
	
	static ApplicationContext ac = null;
	
	static {
		try{
			ServletContext servletContext = ServletActionContext
			.getServletContext();
			ac = WebApplicationContextUtils
			.getRequiredWebApplicationContext(servletContext);
		}catch(Exception e) {
			String[]  p = {"classpath*:applicationContext.xml","classpath*:resources/**/*-spring.xml"};
			ac = new ClassPathXmlApplicationContext(p);
		}

	}

	public static Object getBean(String beanID){
		return ac.getBean(beanID);
	}
	
	public static ApplicationContext getAc() {
		return ac;
	}
}
