package com.map222.sfmc.socialnetworkfx.domain.validators.exceptions;

/**
 * Custom exception class denoting a provided object has invalid attributes or state
 */
public class InvalidEntityException extends IllegalArgumentException{
    public InvalidEntityException() {

    }

    public InvalidEntityException (String message) {
        super(message);
    }

    public InvalidEntityException (String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEntityException (Throwable cause) {
        super(cause);
    }
}
