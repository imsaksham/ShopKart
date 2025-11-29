package com.shopkart.project.exceptions;

import java.time.LocalDateTime;

public class MyErrorDetails {

	private LocalDateTime timeStamp;
	private String message;
	private Integer errorCode;
	private String details;

	public MyErrorDetails() {
		
	}

	public MyErrorDetails(LocalDateTime timeStamp, String message, Integer errorCode, String details) {
		this.timeStamp = timeStamp;
		this.message = message;
		this.errorCode = errorCode;
		this.details = details;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
