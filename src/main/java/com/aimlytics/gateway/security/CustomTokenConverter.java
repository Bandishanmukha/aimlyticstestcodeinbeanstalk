package com.aimlytics.gateway.security;

import static com.aimlytics.gateway.constants.AppConstants.AUTHENTICATION_SUCCESS_1102;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.aimlytics.gateway.model.AppUser;
import com.aimlytics.gateway.model.Status;
import com.aimlytics.gateway.repositories.AppUserRepository;

public class CustomTokenConverter extends JwtAccessTokenConverter {

	private static final Logger log = LoggerFactory.getLogger(CustomTokenConverter.class);

	private AppUserRepository appUserRepository;

	public CustomTokenConverter() {

	}

	public CustomTokenConverter(AppUserRepository appUserRepository) {
		this.appUserRepository = appUserRepository;
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

		final Map<String, Object> additionalInfo = new HashMap<>();
		additionalInfo.put("perm", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList()));
		log.info("Id :::: {}", authentication.getUserAuthentication().getName());
		Optional<AppUser> appUserOptional = appUserRepository
				.findByUserName(authentication.getUserAuthentication().getName());
		if (appUserOptional.isPresent()) {
			AppUser appUser = appUserOptional.get();
			log.info(">>> User Id:::  {}", appUserOptional.get().getId());
			String userId = String.valueOf(appUser.getId());
			additionalInfo.put("userId", StringUtils.repeat("0", 10 - userId.length()) + userId);
			additionalInfo.put("userRefId", appUser.getRefId());
			additionalInfo.put("name", appUser.fullName());
			additionalInfo.put("userType", appUser.getUserType());
			additionalInfo.put("messageCode", AUTHENTICATION_SUCCESS_1102);
			additionalInfo.put("permissions", appUser.buildPermissions());
			// setting login attempts count to zero
			appUser.setLoginAttempts(0);
			appUser.setUserStatus(Status.ACTIVE);
			appUserRepository.save(appUser);


		}
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

		return super.enhance(accessToken, authentication);
	}
}