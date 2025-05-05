package com.abbos.moviego.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-05
 */
public class UserNotFoundException extends BaseException {

    public UserNotFoundException(String message, Object... args) {
        super(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", message, args);
    }
}
