package com.map222.sfmc.socialnetworkfx.domain.validators.exceptions;

/**
 * Custom exception class denoting an invalid ID was used to access Entity-derived objects
 */
public class InvalidIDException extends IllegalArgumentException {
    public InvalidIDException() {

    }

    public InvalidIDException (String message) {
        super(message);
    }

    public InvalidIDException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidIDException (Throwable cause) {
        super(cause);
    }
}
