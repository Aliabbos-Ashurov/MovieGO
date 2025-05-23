package com.abbos.moviego.config.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
class CustomAuthenticationFailureHandlerTest {

    @Test
    void onAuthenticationFailure_redirectsWithEncodedError() throws IOException {
        CustomAuthenticationFailureHandler handler = new CustomAuthenticationFailureHandler();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException exception = mock(AuthenticationException.class);

        when(request.getContextPath()).thenReturn("/app");

        handler.onAuthenticationFailure(request, response, exception);

        String expectedError = URLEncoder.encode("Invalid credentials", StandardCharsets.UTF_8);
        verify(response).sendRedirect("/app/auth/login?error=" + expectedError);
    }
}
