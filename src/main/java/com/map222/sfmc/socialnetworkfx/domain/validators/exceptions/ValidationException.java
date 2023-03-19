package com.map222.sfmc.socialnetworkfx.domain.validators.exceptions;

/**
 * Custom exception class denoting an object or attribute did not pass desired validation stages
 */
public class ValidationException extends RuntimeException {
    public ValidationException() {

    }

    public ValidationException (String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException (Throwable cause) {
        super(cause);
    }

    public ValidationException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
