package com.abbos.moviego.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.text.MessageFormat;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-03
 */
@Getter
@Setter
public abstract class BaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4858956984823384421L;

    private final HttpStatus httpStatus;
    private final String code;

    public BaseException(HttpStatus httpStatus, String code, String message, Object... args) {
        super(MessageFormat.format(message, args));
        this.httpStatus = httpStatus;
        this.code = code;
    }
}

