package com.aimlytics.gateway.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

@Configuration
@EnableResourceServer
@Order(2)
class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	@Value("#{'${cors.filter.alloworigin}'.split(',')}")
	private List<String> allowOrigin;

	@Value("#{'${cors.filter.allowmethods}'.split(',')}")
	private List<String> allowMethods;

	@Value("${cors.filter.maxage}")
	private Long maxAge;

	@Value("#{'${cors.filter.allowheaders}'.split(',')}")
	private List<String> allowHeaders;

	@Value("${cors.filter.allowcredentials}")
	private boolean allowCredentials;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests()
				.antMatchers("/", "/health", "/admin/notification/verifyEmailNotification/*",
						"/admin/admin/forgotpassword/*", "/admin/swagger-ui.html", "/admin/v3/api-docs", "/admin/swagger-ui/**", "/admin/health",
						"/admin/config/**", "/admin/public/**", "/admin/servEngrSubs/validateSubscription", "/admin/servEngr/forgotpin/**",
						"/customer/customer/**", "/customer/health", "/customer/swagger-ui.html", "/customer/public/**",
						"/customer/v3/api-docs",
						"/bikes/public/**", "/docs/**", "/bikes/home/vehicles/**", "/bikes/iotsec/**", "/bikes/swagger-ui/**",
						"/bikes/v3/api-docs", "/bikes/health", "/bikes/zone/getZoneVersions", "/bikes/geoFenc/getGeoFencesKMLLocation", "/bikes/geoFenc/getGeoFences",
						"/swagger-ui.html", "/swagger-ui/**", "/swagger-ui-custom.html", "/api-docs/**", "/v3/api-docs/**",
						"/notificationclient/health", "/notificationclient/swagger-ui.html", "/notificationclient/v3/api-docs", "/notificationclient/swagger-ui/**", "/notificationclient/config/**",
						"/notificationalerts/health", "/notificationalerts/swagger-ui.html", "/notificationalerts/v3/api-docs", "/notificationalerts/swagger-ui/**", "/notificationalerts/config/**")
				.permitAll().and().authorizeRequests()
				.requestMatchers(new RequestHeaderRequestMatcher("X-APP-ID", "admin"),
						new RequestHeaderRequestMatcher("X-APP-ID", "rides"))
				.permitAll().and().authorizeRequests().anyRequest().authenticated();
	}

}