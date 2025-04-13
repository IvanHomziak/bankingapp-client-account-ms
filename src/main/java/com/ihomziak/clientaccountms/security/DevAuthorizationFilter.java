package com.ihomziak.clientaccountms.security;

import static com.ihomziak.clientaccountms.util.constants.Endpoints.SwaggerEndpoints.*;

import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;

@Profile("dev")
public class DevAuthorizationFilter extends BaseAuthorizationFilter {

	public DevAuthorizationFilter(AuthenticationManager authManager, Environment environment) {
		super(authManager, environment);
	}

	@Override
	protected boolean isPublicEndpoint(String path) {
		return path.startsWith(SWAGGER_UI) ||
			path.startsWith(SWAGGER_UI_HTML) ||
			path.startsWith(SWAGGER_UI_INDEX) ||
			path.startsWith(SWAGGER_API_DOCS_V2) ||
			path.startsWith(SWAGGER_API_DOCS_V3) ||
			path.startsWith(SWAGGER_RESOURCES) ||
			path.startsWith(SWAGGER_WEBJARS);
	}
}