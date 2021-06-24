package com.aimlytics.gateway.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.aimlytics.gateway.repositories.AppUserRepository;

@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private AppUserRepository appUserRepository;
	
	@Autowired
	private MessageSource messageSource;

	@Override
	public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager).
				accessTokenConverter(
						jwtAccessTokenConverter()).exceptionTranslator(new AppLoginExceptionTranslator(appUserRepository, messageSource)) ;
		
		 endpoints.addInterceptor(new HandlerInterceptorAdapter() {
	            @Override
	            public boolean preHandle(HttpServletRequest hsr, HttpServletResponse rs, Object o) throws Exception {
	                rs.setHeader("Access-Control-Allow-Origin", "*");
	                rs.setHeader("Access-Control-Allow-Methods", "POST,PUT,GET,OPTIONS,DELETE");
	                rs.setHeader("Access-Control-Max-Age", "3600");
	                rs.setHeader("Access-Control-Allow-Headers", "*");
	                return true;
	            }
	        });
	}

	@Override
	public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("client").secret("$2a$10$MC/aEp6rmXnA4sMq1skzv.Y2HDTGCb2W1UsF3C3M1l.VrJ9oQW7eC")
				.scopes("read", "write").authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds(60 * 60 * 12).refreshTokenValiditySeconds(60 * 60 * 24 * 30);
	}
	
	

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new CustomTokenConverter(appUserRepository);
		converter.setSigningKey("abc");
		return converter;
	}

	@Bean
	@Primary
	public AuthorizationServerTokenServices tokenServices() {
		final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setSupportRefreshToken(true);
		defaultTokenServices.setAccessTokenValiditySeconds(60 * 60 * 12);
		defaultTokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 30);
		return defaultTokenServices;
	}
	
	
	

}