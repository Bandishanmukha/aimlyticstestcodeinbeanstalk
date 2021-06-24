package com.aimlytics.gateway.api;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeApi {
	
	@GetMapping("/health")
	public String home(@AuthenticationPrincipal String name) {
		return "Up and Running" + name;
	}

}
