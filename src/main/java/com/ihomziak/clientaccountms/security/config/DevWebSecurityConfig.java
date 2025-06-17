package com.ihomziak.clientaccountms.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Profile("dev")
@Configuration
@EnableWebSecurity
public class DevWebSecurityConfig extends BaseWebSecurityConfig {

	public DevWebSecurityConfig(Environment environment) {
		super(environment);
	}

	@Bean
	public SecurityFilterChain devSecurityFilterChain(HttpSecurity http) throws Exception {
		return configure(http, true);
	}
}
