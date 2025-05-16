package com.abbos.moviego.config.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Handles failed authentication attempts by redirecting to the login page with an error message.
 * Implements {@link AuthenticationFailureHandler}.
 *
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-09
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String message = exception.getMessage();
        response.sendRedirect(request.getContextPath() + "/auth/login?error=" + message);
    }
}
