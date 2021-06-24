package com.aimlytics.gateway.service.impl;

import java.util.List;
import java.util.stream.Collectors;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aimlytics.gateway.model.AppUser;
import com.aimlytics.gateway.model.AppRoles;
import com.aimlytics.gateway.model.Status;
import com.aimlytics.gateway.repositories.AppUserRepository;

@Service("appUserDetailsService")
public class AppUserDetailsServiceImpl implements UserDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(AppUserDetailsServiceImpl.class);

	@Autowired
	private AppUserRepository appUserRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) {
		logger.info(" UserName ::: {}", username);
		AppUser appUser = appUserRepository.findByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));
		return new User(appUser.getUserName(), appUser.getPassword(), appUser.isEnabled(), true, true,
				appUser.getUserStatus() != Status.LOCKED, findAllAuthorities(appUser.getRoles()));
	}

	private List<GrantedAuthority> findAllAuthorities(List<AppRoles> appUserRoles) {
		return appUserRoles.stream().map(AppRoles::getName).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

}
