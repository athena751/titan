package com.hp.app.exception;


@SuppressWarnings("serial")
public class BaseDaoException extends BaseException {

	public BaseDaoException() {
	}

	public BaseDaoException(String message) {
		super(message);
	}

	public BaseDaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseDaoException(Throwable cause) {
		super(cause);
	}

	public BaseDaoException(String errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}
}
