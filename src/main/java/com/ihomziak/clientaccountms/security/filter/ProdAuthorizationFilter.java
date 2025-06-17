package com.ihomziak.clientaccountms.security.filter;


import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;

@Profile("prod")
public class ProdAuthorizationFilter extends BaseAuthorizationFilter {

	public ProdAuthorizationFilter(AuthenticationManager authManager, Environment environment) {
		super(authManager, environment);
	}

	@Override
	protected boolean isPublicEndpoint(String path) {
		return false;
	}
}
