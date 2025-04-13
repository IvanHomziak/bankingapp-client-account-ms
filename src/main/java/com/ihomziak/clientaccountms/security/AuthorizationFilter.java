package com.ihomziak.clientaccountms.security;

import static com.ihomziak.clientaccountms.util.constants.Endpoints.SwaggerEndpoints.*;

import com.ihomziak.bankingapp.common.api.JwtClaimsParser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final Environment environment;

    public AuthorizationFilter(AuthenticationManager authManager, Environment environment) {
        super(authManager);
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
        HttpServletResponse res,
        FilterChain chain) throws IOException, ServletException {

        String path = req.getRequestURI();

        // Skip security filter for Swagger-related endpoints
        if (isPublicEndpoint(path)) {
            chain.doFilter(req, res);
            return;
        }

        String headerName = environment.getProperty("authorization.token.header.name", "Authorization");
        String tokenPrefix = environment.getProperty("authorization.token.header.prefix", "Bearer ");

        String header = req.getHeader(headerName);

        if (header == null || !header.startsWith(tokenPrefix)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(header, tokenPrefix);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String header, String tokenPrefix) {
        String token = header.replace(tokenPrefix, "").trim();
        String secret = environment.getProperty("token.secret");

        if (secret == null || token.isEmpty()) return null;

        try {
            JwtClaimsParser jwtClaimsParser = new JwtClaimsParser(token, secret);
            String userId = jwtClaimsParser.getJwtSubject();
            List authorities = jwtClaimsParser.getUserAuthorities().stream().toList();

            if (userId == null) return null;

            return new UsernamePasswordAuthenticationToken(userId, null, authorities);
        } catch (Exception e) {
            System.out.println("JWT parsing failed: " + e.getMessage());
            return null;
        }
    }

    private boolean isPublicEndpoint(String path) {
        return path.startsWith(SWAGGER_UI) ||
            path.startsWith(SWAGGER_UI_HTML) ||
            path.startsWith(SWAGGER_UI_INDEX) ||
            path.startsWith(SWAGGER_API_DOCS_V2) ||
            path.startsWith(SWAGGER_API_DOCS_V3) ||
            path.startsWith(SWAGGER_RESOURCES) ||
            path.startsWith(SWAGGER_WEBJARS);
    }
}
