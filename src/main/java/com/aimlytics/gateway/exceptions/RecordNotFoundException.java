package com.aimlytics.gateway.exceptions;

public class RecordNotFoundException extends GenericException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2124505854332275117L;
	
	public RecordNotFoundException(String msg) {
		super(msg);
	}
	
	public RecordNotFoundException(String msg, String msgCode) {
		super(msg, msgCode);
	}

}
