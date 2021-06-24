package com.aimlytics.gateway.model;

public enum UserType {
	
	SUPER_ADMIN(new String[] {"SUP_ADMIN" }), BUSINESS_ADMIN(new String[] { "BP_ADMIN" }),
	ENERGIZING_ADMIN(new String[] { "EP_ADMIN" }), BUDE_RIDER(new String[] { "BUD_E_RIDER" }),
	SERVICE_ENGINEER(new String[] { "SERV_ENG" }), PARTNER_RIDER(new String[] { "PART_RIDER" });

	private String[] roles;

	private UserType(String[] roles) {
		this.roles=roles;
	}

	public String[] getRoles() {
		return roles;
	}

	
}
