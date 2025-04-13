package com.ihomziak.clientaccountms.security;

import static com.ihomziak.clientaccountms.util.constants.Endpoints.AccountEndpoints.*;
import static com.ihomziak.clientaccountms.util.constants.Endpoints.ClientEndpoints.*;
import static com.ihomziak.clientaccountms.util.constants.Endpoints.SwaggerEndpoints.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class WebSecurity {

    Environment environment;

    public WebSecurity(Environment environment) {
        this.environment = environment;
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {

        // Configure AuthenticationManagerBuilder
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        // Get AuthenticationManager
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                    (auth) ->
                        auth.requestMatchers(
                                SWAGGER_UI,
                                SWAGGER_UI_HTML,
                                SWAGGER_UI_INDEX,
                                SWAGGER_API_DOCS_V2,
                                SWAGGER_API_DOCS_V3,
                                SWAGGER_RESOURCES,
                                SWAGGER_WEBJARS
                        ).permitAll()
                            .requestMatchers(HttpMethod.PUT, API_CLIENT_V1 + ADD_CLIENT).authenticated()
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
                            .requestMatchers(HttpMethod.GET, API_ACCOUNT_V1).authenticated()


                )
                .addFilter(new AuthorizationFilter(authenticationManager, environment))
                .authenticationManager(authenticationManager)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }

}

