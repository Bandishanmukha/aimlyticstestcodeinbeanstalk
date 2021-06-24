package com.aimlytics.gateway.exceptions;

public class PasswordException extends GenericException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4867267470143383354L;
	
	public PasswordException(String msg) {
		super(msg);
	}
	
	public PasswordException(String msg, String msgCode) {
		super(msg, msgCode);
	}
	
}
