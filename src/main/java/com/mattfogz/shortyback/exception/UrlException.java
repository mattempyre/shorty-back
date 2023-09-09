package com.mattfogz.shortyback.exception;

/**
 * Custom exception class for URL-related issues.
 * This exception is a specialization of the runtime exception,
 * allowing the application to handle URL-specific issues with more granularity.
 */
public class UrlException extends RuntimeException {

    /**
     * Constructor that initializes the exception with a specific error message.
     *
     * @param message A detailed message describing the reason for the exception.
     *                This message can be used for logging or to inform users of the specific error encountered.
     */
    public UrlException(String message) {
        super(message);
    }
}
