package com.aimlytics.gateway;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class GatewayConfiguration {
	
	@Bean("messageSource")
	protected MessageSource getMessageSource() {
		ReloadableResourceBundleMessageSource messageResource = new ReloadableResourceBundleMessageSource();
		messageResource.setBasename("classpath:message/codes");
		messageResource.setDefaultEncoding("UTF-8");
		return messageResource;
	}
}
