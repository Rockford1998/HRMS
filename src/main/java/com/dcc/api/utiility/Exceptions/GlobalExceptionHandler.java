package com.dcc.api.utiility.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import com.dcc.api.utiility.ResponseHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException e, WebRequest request) {
        String rootCauseMessage = e.getRootCause().getMessage();
        String originatingClass = e.getStackTrace()[0].getClassName();

        // todo - add a enum to maintain all the modules name 
        if (rootCauseMessage != null && rootCauseMessage.contains("skill_name_key")) {
            return ResponseHandler.responseBuilder("Skill name is already present in the database", HttpStatus.CONFLICT, null);
        } else {
            return ResponseHandler.responseBuilder("Data integrity violation", HttpStatus.CONFLICT, null);
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e, WebRequest request) {
        return ResponseHandler.responseBuilder("Constraint violation:" + e.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(Exception e, WebRequest request) {
        return ResponseHandler.responseBuilder("ResourceNotFoundException: " + e.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception e, WebRequest request) {
        return ResponseHandler.responseBuilder("An unexpected error occurred", HttpStatus.BAD_REQUEST, null);
    }

}
