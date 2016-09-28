package com.hp.titan.notify;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ContextListener extends HttpServlet implements
		ServletContextListener {
	private Timer timer = null;
	private Calendar cal = Calendar.getInstance();

	public void contextInitialized(ServletContextEvent event) {
		WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		timer = new java.util.Timer(true);
		System.out.println(cal.getTime() + "Timer get started");
		
		Calendar calendar = Calendar.getInstance();  
		calendar.set(Calendar.HOUR_OF_DAY, 23);  
		calendar.set(Calendar.MINUTE, 55);  
		calendar.set(Calendar.SECOND, 0);  
		Date time = calendar.getTime();  
		
		Calendar calendar1 = Calendar.getInstance();  
		calendar1.set(Calendar.YEAR, 2016);
		calendar1.set(Calendar.MONTH, 1);
		calendar1.set(Calendar.DAY_OF_MONTH, 20);
		calendar1.set(Calendar.HOUR_OF_DAY, 0);  
		calendar1.set(Calendar.MINUTE, 30);  
		calendar1.set(Calendar.SECOND, 0);  
		Date time1 = calendar1.getTime(); 
		
//		timer.schedule(new NotifyTaskAction(springContext), time, 24 * 60 * 60 * 1000);
//		timer.schedule(new RefreshTaskAction(springContext), time1,  24 * 60 * 60 * 1000);
//		timer.schedule(new RefreshDeviceAction(springContext), time1,  24 * 60 * 60 * 1000);		
		System.out.println(cal.getTime() + "added schedule task");
	}
	
	public void contextDestroyed(ServletContextEvent event) {
		timer.cancel();
		System.out.println(cal.getTime() + "Timer destoried");
	}
}
