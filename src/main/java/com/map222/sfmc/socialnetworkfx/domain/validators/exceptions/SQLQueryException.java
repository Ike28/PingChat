package com.map222.sfmc.socialnetworkfx.domain.validators.exceptions;

/**
 * Custom exception class denoting SQL errors encountered while accessing the database
 */
public class SQLQueryException extends RuntimeException {
    public SQLQueryException() {

    }

    public SQLQueryException (String message) {
        super(message);
    }

    public SQLQueryException (String message, Throwable cause) {
        super(message, cause);
    }

    public SQLQueryException (Throwable cause) {
        super(cause);
    }
}
