package com.mattfogz.shortyback.controller;

import com.mattfogz.shortyback.exception.UrlException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * A global exception handler class that provides centralized handling of exceptions 
 * thrown across the entire application, rather than handling exceptions in every 
 * individual controller.
 */
@ControllerAdvice  // Annotation that makes this class a central place for handling exceptions across multiple controllers
public class GlobalExceptionHandler {

    /**
     * Handle custom UrlExceptions thrown within the application.
     *
     * @param e The UrlException instance containing the error details.
     * @return ResponseEntity with the error message and a BAD_REQUEST status.
     */
    @ExceptionHandler(UrlException.class)  // This method is invoked when the specified exception (UrlException) is thrown
    public ResponseEntity<Map<String, String>> handleUrlException(UrlException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", e.getMessage());

        // Return a response entity with the error message and a HTTP status of BAD_REQUEST
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle IllegalArgumentExceptions thrown within the application.
     * 
     * @param e The IllegalArgumentException instance containing the error details.
     * @return ResponseEntity with the error message and a BAD_REQUEST status.
     */
    @ExceptionHandler(IllegalArgumentException.class)  // This method is invoked when the specified exception (IllegalArgumentException) is thrown
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", e.getMessage());

        // Return a response entity with the error message and a HTTP status of BAD_REQUEST
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
