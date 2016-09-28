package com.hp.app.exception;

@SuppressWarnings("serial")
public class BaseException extends Exception {

	private String errorCode;

	public String getErrorCode() {
		return this.errorCode;
	}

	public BaseException() {
	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(String message,String stateCode) {
		super(message);
		this.setErrorCode(stateCode);
	}
	
	public BaseException(Throwable arg0) {
		super(arg0);
	}

	public BaseException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public BaseException(String errorCode, String arg0, Throwable arg1) {
		super(arg0, arg1);
		this.errorCode = errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
