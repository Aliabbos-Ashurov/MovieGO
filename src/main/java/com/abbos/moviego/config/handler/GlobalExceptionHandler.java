package com.abbos.moviego.config.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-09
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound(NoHandlerFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("/common/error");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        modelAndView.addObject("message", "404 PAGE NOT FOUND");
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleModificationException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("/common/error");
        modelAndView.setStatus(HttpStatus.FORBIDDEN);
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }
}
