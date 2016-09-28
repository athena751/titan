package com.hp.titan.server.action;
public class AppException extends Exception {
	public AppException(String str) {
		new Exception(str);
	}

	public AppException(String str, Exception e) {
		new Exception(str, e);
		e.printStackTrace();
	}
}
