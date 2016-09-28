package com.hp.app.config;

import java.io.IOException;
import java.util.Properties;

/**
 * Porject titan
 * @author xu.yang@hp.com 
 * 17 Sep 2013
 */
public class SysConfiger {
	
	static Properties prop = new Properties();
	
	static {
		try {
			prop.load(SysConfiger.class.getResourceAsStream("/application.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String key){
		return prop.getProperty(key);
	}
	
	public static boolean isOpentitan(){
		return Boolean.parseBoolean(SysConfiger.getProperty("titanOpenFlag"));
	}
	
	public static boolean isOpenIO(){
		return Boolean.parseBoolean(SysConfiger.getProperty("ioOpenFlag"));
	}

	/**
	 * Get the path of application context
	 */
	public static String getAppPath() {
		return SysConfiger.class.getResource("/").toString();
	}
}