package com.aimlytics.gateway.exceptions;

public class GenericException extends RuntimeException{

	private static final long serialVersionUID = -4431042891936223335L;
	
	private String msgCode;

	public String getMsgCode() {
		return msgCode;
	}
	
	public GenericException(String msg) {
		super(msg);
	}

	public GenericException(String msg, String msgCode) {
		super(msg);
		this.msgCode = msgCode;
	}
	
	public GenericException setMsgCode(String msgCode) {
		this.msgCode = msgCode;
		return this;
	}
}
