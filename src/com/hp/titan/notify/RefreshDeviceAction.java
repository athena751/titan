package com.hp.titan.notify;

import java.util.TimerTask;

import org.springframework.web.context.WebApplicationContext;

import com.hp.titan.server.service.ServerService;

public class RefreshDeviceAction extends TimerTask {
	private ServerService serverService;

	public RefreshDeviceAction(WebApplicationContext ctx) {
		serverService = (ServerService) ctx.getBean("serverService");
	}

	public void run() {
		try {
			serverService.findAllServerByRefresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
