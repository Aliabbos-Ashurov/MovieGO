package com.abbos.moviego.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-10
 */
public class ModificationNotAllowedException extends BaseException {

    public ModificationNotAllowedException(String message) {
        super(HttpStatus.FORBIDDEN, "MODIFICATION_EXCEPTION", message);
    }
}
