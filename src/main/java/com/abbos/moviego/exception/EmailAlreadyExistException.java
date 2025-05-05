package com.abbos.moviego.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
public class EmailAlreadyExistException extends BaseException {

    public EmailAlreadyExistException(String message, Object... args) {
        super(HttpStatus.CONFLICT, "EMAIL_ALREADY_EXIST", message, args);
    }
}
