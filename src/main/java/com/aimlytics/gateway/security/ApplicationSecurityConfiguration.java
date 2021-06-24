package com.aimlytics.gateway.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Order(100)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

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

	@Autowired
	@Qualifier("appUserDetailsService")
	private UserDetailsService userDetailsService;

	@Autowired
	public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.cors().disable().antMatcher("/**").authorizeRequests()
				.antMatchers("/", "/health", "/admin/verifyEmailNotification").permitAll().and().authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS, "/oauth/token").permitAll()
				.anyRequest().authenticated().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable().cors().disable();

	}

	/*
	 * @Bean public CorsConfigurationSource corsConfigurationSource() { final
	 * CorsConfiguration configuration = new CorsConfiguration();
	 * configuration.setAllowedOrigins(allowOrigin);
	 * configuration.setAllowedMethods(allowMethods);
	 * configuration.setAllowCredentials(true); configuration.setMaxAge(maxAge);
	 * configuration.setAllowedHeaders(allowHeaders);
	 * configuration.addAllowedHeader("WWW-Authenticate"); final
	 * UrlBasedCorsConfigurationSource source = new
	 * UrlBasedCorsConfigurationSource(); source.registerCorsConfiguration("/**",
	 * configuration); return source; }
	 */

}
