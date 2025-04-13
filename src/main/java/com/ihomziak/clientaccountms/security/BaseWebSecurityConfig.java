package com.ihomziak.clientaccountms.security;

import static com.ihomziak.clientaccountms.util.constants.Endpoints.AccountEndpoints.*;
import static com.ihomziak.clientaccountms.util.constants.Endpoints.ClientEndpoints.*;
import static com.ihomziak.clientaccountms.util.constants.Endpoints.SwaggerEndpoints.*;

import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public abstract class BaseWebSecurityConfig {

    protected final Environment environment;

    protected BaseWebSecurityConfig(Environment environment) {
        this.environment = environment;
    }

    protected SecurityFilterChain configure(HttpSecurity http, boolean permitSwagger) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((auth) -> {
                if (permitSwagger) {
                    auth.requestMatchers(SWAGGER_UI, SWAGGER_UI_HTML, SWAGGER_UI_INDEX,
                            SWAGGER_API_DOCS_V2, SWAGGER_API_DOCS_V3,
                            SWAGGER_RESOURCES, SWAGGER_WEBJARS).permitAll()
                        .requestMatchers(HttpMethod.POST, API_CLIENT_V1 + ADD_CLIENT).permitAll()
                        .requestMatchers(HttpMethod.GET, API_CLIENT_V1 + GET_CLIENT).permitAll()
                        .requestMatchers(HttpMethod.GET, API_CLIENT_V1).permitAll()
                        .requestMatchers(HttpMethod.DELETE, API_CLIENT_V1 + DELETE_CLIENT).permitAll()
                        .requestMatchers(HttpMethod.PATCH, API_CLIENT_V1 + UPDATE_CLIENT).permitAll()
                        .requestMatchers(HttpMethod.GET, API_CLIENT_V1 + SEARCH_CLIENT).permitAll()
                        .requestMatchers(HttpMethod.GET, API_CLIENT_V1 + COUNT_CLIENTS_BY_LAST_NAME).permitAll()

                        .requestMatchers(HttpMethod.GET, API_ACCOUNT_V1 + GET_ACCOUNT).permitAll()
                        .requestMatchers(HttpMethod.GET, API_ACCOUNT_V1 + GET_CLIENT_ACCOUNTS).permitAll()
                        .requestMatchers(HttpMethod.POST, API_ACCOUNT_V1).permitAll()
                        .requestMatchers(HttpMethod.PATCH, API_ACCOUNT_V1).permitAll()
                        .requestMatchers(HttpMethod.DELETE, API_ACCOUNT_V1 + DELETE_ACCOUNT).permitAll()
                        .requestMatchers(HttpMethod.GET, API_ACCOUNT_V1).permitAll();
                } else {
                    auth.requestMatchers(SWAGGER_UI, SWAGGER_UI_HTML, SWAGGER_UI_INDEX,
                            SWAGGER_API_DOCS_V2, SWAGGER_API_DOCS_V3,
                            SWAGGER_RESOURCES, SWAGGER_WEBJARS).authenticated()
                        .requestMatchers(HttpMethod.POST, API_CLIENT_V1 + ADD_CLIENT).authenticated()
                        .requestMatchers(HttpMethod.GET, API_CLIENT_V1 + GET_CLIENT).authenticated()
                        .requestMatchers(HttpMethod.GET, API_CLIENT_V1).authenticated()
                        .requestMatchers(HttpMethod.DELETE, API_CLIENT_V1 + DELETE_CLIENT).authenticated()
                        .requestMatchers(HttpMethod.PATCH, API_CLIENT_V1 + UPDATE_CLIENT).authenticated()
                        .requestMatchers(HttpMethod.GET, API_CLIENT_V1 + SEARCH_CLIENT).authenticated()
                        .requestMatchers(HttpMethod.GET, API_CLIENT_V1 + COUNT_CLIENTS_BY_LAST_NAME).authenticated()

                        .requestMatchers(HttpMethod.GET, API_ACCOUNT_V1 + GET_ACCOUNT).authenticated()
                        .requestMatchers(HttpMethod.GET, API_ACCOUNT_V1 + GET_CLIENT_ACCOUNTS).authenticated()
                        .requestMatchers(HttpMethod.POST, API_ACCOUNT_V1).authenticated()
                        .requestMatchers(HttpMethod.PATCH, API_ACCOUNT_V1).authenticated()
                        .requestMatchers(HttpMethod.DELETE, API_ACCOUNT_V1 + DELETE_ACCOUNT).authenticated()
                        .requestMatchers(HttpMethod.GET, API_ACCOUNT_V1).authenticated();
                }
            })
            .addFilter(getAuthorizationFilter(authenticationManager))
            .authenticationManager(authenticationManager)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    private BasicAuthenticationFilter getAuthorizationFilter(AuthenticationManager authManager) {
        if (environment.acceptsProfiles(Profiles.of("dev"))) {
            return new DevAuthorizationFilter(authManager, environment);
        } else {
            return new ProdAuthorizationFilter(authManager, environment);
        }
    }

}