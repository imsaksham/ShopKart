package com.shopkart.project.exceptions;

public class ServiceException extends RuntimeException {

	boolean formatted;
	int errorCode;

	private static final long serialVersionUID = 1L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(boolean hasFormattedMessage, String message) {
		super(message);
		this.formatted = hasFormattedMessage;
	}

	public ServiceException(boolean hasFormattedMessage, Integer errorCode, String message) {
		super(message);
		this.formatted = hasFormattedMessage;
		this.errorCode = errorCode;
	}

	public boolean isFormatted() {
		return formatted;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

}
