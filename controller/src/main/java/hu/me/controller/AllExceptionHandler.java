package hu.me.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AllExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AllExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception) {
        LOG.error("Unexpected error occurred: " + exception.getMessage(), exception);
        return "error";
    }


}
