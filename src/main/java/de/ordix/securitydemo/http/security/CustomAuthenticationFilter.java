package de.ordix.securitydemo.http.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private static final String HIDDEN_KEY = "Bearer demo123";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean authenticated = authenticated(request);
        if (!authenticated) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
        filterChain.doFilter(request, response);
    }

    private boolean authenticated(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return HIDDEN_KEY.equals(token);
    }


}
