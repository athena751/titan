package com.hp.app.exception;


@SuppressWarnings("serial")
public class BaseServiceException extends BaseException {

	public BaseServiceException() {
	}

	public BaseServiceException(String arg0) {
		super(arg0);
	}

	public BaseServiceException(Throwable arg0) {
		super(arg0);
	}

	public BaseServiceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public BaseServiceException(String errorCode, String message,
			Throwable throwable) {
		super(errorCode, message, throwable);
	}
}
