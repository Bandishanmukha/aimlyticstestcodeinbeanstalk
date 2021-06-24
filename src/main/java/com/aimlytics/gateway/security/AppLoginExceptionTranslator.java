package com.aimlytics.gateway.security;

import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aimlytics.gateway.model.AppUser;
import com.aimlytics.gateway.model.Status;
import com.aimlytics.gateway.repositories.AppUserRepository;
import static com.aimlytics.gateway.constants.AppConstants.AUTHENTICATION_FAIL_2101;
import static com.aimlytics.gateway.constants.AppConstants.ACCOUNT_BLOCKED_2102;
import static com.aimlytics.gateway.constants.AppConstants.INVALID_USERNAME_2103;
import static com.aimlytics.gateway.constants.AppConstants.INVLID_TOKEN_4105;

public class AppLoginExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

	private static final Logger log = LoggerFactory.getLogger(AppLoginExceptionTranslator.class);

	private AppUserRepository appUserRepository;
	
	private MessageSource messageSource;

	public AppLoginExceptionTranslator(AppUserRepository appUserRepository, MessageSource messageSource) {
		this.appUserRepository = appUserRepository;
		this.messageSource = messageSource;
	}

	@Override
	public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		log.error("Exception ::  {}, {}", request.getParameter("username"), e);
		log.info(">> Security :: {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		String userName = request.getParameter("username");
		Optional<AppUser> appUserOptional = appUserRepository.findByUserName(userName);
		if (appUserOptional.isPresent()) {
			AppUser appUser = appUserOptional.get();
			if (!appUser.getUserStatus().equals(Status.LOCKED)) {
				appUser.setLoginAttempts(appUser.getLoginAttempts() + 1);
				if (appUser.getLoginAttempts() == 5)
					appUser.setUserStatus(Status.LOCKED);
				appUserRepository.save(appUser);
				LoginFailureException loginFailEx = new LoginFailureException(String.valueOf(5 - appUser.getLoginAttempts()));
				loginFailEx.addAdditionalInformation("messageCode", AUTHENTICATION_FAIL_2101);
				loginFailEx.addAdditionalInformation("message", messageSource.getMessage(AUTHENTICATION_FAIL_2101, null, Locale.ENGLISH));
				return new ResponseEntity<>( loginFailEx,HttpStatus.UNAUTHORIZED);
			}
			
			UserAccountLockedException userAccLockedEx = new UserAccountLockedException(e.getMessage());
			userAccLockedEx.addAdditionalInformation("messageCode", ACCOUNT_BLOCKED_2102);
			userAccLockedEx.addAdditionalInformation("message", messageSource.getMessage(ACCOUNT_BLOCKED_2102, null, Locale.ENGLISH));
			return new ResponseEntity<>(userAccLockedEx, HttpStatus.UNAUTHORIZED);
		}
		
		OAuth2Exception ex = null;
		if (e instanceof InvalidTokenException) {
			ex = OAuth2Exception.create("invalid_token",
					messageSource.getMessage(INVLID_TOKEN_4105, null, Locale.ENGLISH));
			ex.addAdditionalInformation("messageCode", INVLID_TOKEN_4105);
					
		} else if (e instanceof InvalidGrantException) {
			ex = OAuth2Exception.create("invalid_grant",
					messageSource.getMessage(INVALID_USERNAME_2103, null, Locale.ENGLISH));
			ex.addAdditionalInformation("messageCode", INVALID_USERNAME_2103);
		}
		
		log.info(">> exception class :: {}", e.getClass());
		return new ResponseEntity<>(ex ,HttpStatus.UNAUTHORIZED);
	}

	static class LoginFailureException extends OAuth2Exception {

		private static final long serialVersionUID = -2255218919408482848L;

		private int loginAttempts;

		public LoginFailureException(String msg) {
			super(msg);
		}

		public LoginFailureException(String msg, int loginAttempts) {
			super(msg);
			this.loginAttempts = loginAttempts;
		}

		public int getLoginAttempts() {
			return loginAttempts;
		}

		public void setLoginAttempts(int loginAttempts) {
			this.loginAttempts = loginAttempts;
		}

		@Override
		public String getOAuth2ErrorCode() {
			return "remaining_attempts";
		}

	}

	static class UserAccountLockedException extends OAuth2Exception {

		private static final long serialVersionUID = -2255218919408482848L;

		public UserAccountLockedException(String msg) {
			super(msg);
		}

	}

}
