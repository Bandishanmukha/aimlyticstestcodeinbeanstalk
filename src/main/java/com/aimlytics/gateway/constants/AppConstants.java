package com.aimlytics.gateway.constants;

public class AppConstants {
	
	private AppConstants() {}
	
	/*
	 * 
		S M 0 0
		
		S - for status, valid status code are 
			1 <-- Success
			2 <-- fail
			3 <-- not found
			4 <-- bad request (required field exception
			5 <-- server error
		
		M - for module, valid module code are
		 	1 <-- Gateway
			2 <-- Bikes
			3 <-- Rider Module
			4 <-- Admin Module
		
		Rest two digits are number start from 01 to 99
	 
	 */
	
	//success
	public static final String PIN_RESET_SUCCESS_1101="1101";
	public static final String AUTHENTICATION_SUCCESS_1102="1102";
	public static final String TOKEN_SENT_1103="1103";
	public static final String PSWD_UPDATED_1104="1104";
	
	
	//fail
	public static final String AUTHENTICATION_FAIL_2101="2101";
	public static final String ACCOUNT_BLOCKED_2102="2102";
	public static final String INVALID_USERNAME_2103="2103";
	
	//not found
	public static final String USER_NOT_FOUND_3101="3101";
	
	
	//bad request
	public static final String INVALID_PIN_FORMAT_4101 = "4101";
	public static final String PINS_MISMATCH_4102 = "4102";
	public static final String NEW_PIN_REQUIRED_4103="4103";
	public static final String CONFIRM_PIN_REQUIRED_4104="4104";
	public static final String TOKEN_EXPIRED_401="401";
	public static final String INVLID_TOKEN_4105="4105";
	public static final String WRONG_CURRENT_PASSWORD_4106="4106";
	public static final String CURRENT_PIN_REQUIRED_4107="4107";
	public static final String USERNAME_REQUIRED_4108="4108";

	
	
	
	//server error
	public static final String RIDER_SERVER_ERR_5300="5300";
	public static final String GATEWAY_SERVER_ERR_5100="5100";
	public static final String ADMIN_SERVER_ERR_5400="5400";
	public static final String BIKE_SERVER_ERR_5200="5200";
}
