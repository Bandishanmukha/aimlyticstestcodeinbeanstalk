package com.aimlytics.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
	
	@Test
	public void encodPassword() {
		System.out.println(new BCryptPasswordEncoder().encode("password"));
		
		//.out.println(BCrypt.checkpw("clientpassword", "$2a$10$MC/aEp6rmXnA4sMq1skzv.Y2HDTGCb2W1UsF3C3M1l.VrJ9oQW7eC"));
	}

}
