package com.map222.sfmc.socialnetworkfx.domain.validators.exceptions;

/**
 * Custom exception class denoting illegal access to an entity labelled as deleted
 */
public class DeletedEntityException extends RuntimeException{
    public DeletedEntityException() {

    }

    public DeletedEntityException (String message) {
        super(message);
    }

    public DeletedEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeletedEntityException (Throwable cause) {
        super(cause);
    }

    public DeletedEntityException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
