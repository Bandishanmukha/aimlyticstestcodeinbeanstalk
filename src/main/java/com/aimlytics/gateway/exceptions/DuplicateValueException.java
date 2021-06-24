package com.aimlytics.gateway.exceptions;

public class DuplicateValueException extends GenericException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8890312132097126941L;
	
	public DuplicateValueException(String msg) {
		super(msg);
	}

	public DuplicateValueException(String msg, String msgCode) {
		super(msg, msgCode);
	}

}
