package com.abbos.moviego.config.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleNotFound() {
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/invalid", HttpHeaders.EMPTY);
        ModelAndView result = exceptionHandler.handleNotFound(ex);
        assertNotNull(result);
        assertEquals("/common/error", result.getViewName());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatus());
        assertEquals("404 PAGE NOT FOUND", result.getModel().get("message"));
    }

    @Test
    void testHandleModificationException() {
        Exception ex = new Exception("Test exception message");
        ModelAndView result = exceptionHandler.handleModificationException(ex);
        assertNotNull(result);
        assertEquals("/common/error", result.getViewName());
        assertEquals(HttpStatus.FORBIDDEN, result.getStatus());
        assertEquals("Test exception message", result.getModel().get("message"));
    }
}