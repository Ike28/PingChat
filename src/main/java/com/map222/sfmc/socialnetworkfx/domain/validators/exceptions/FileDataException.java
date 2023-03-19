package com.map222.sfmc.socialnetworkfx.domain.validators.exceptions;

/**
 * Custom exception class denoting errors in reading from a file
 * OBSOLETE: Used in file repository classes
 */
public class FileDataException extends RuntimeException {
    public FileDataException() {

    }

    public FileDataException (String message) {
        super(message);
    }

    public FileDataException (String message, Throwable cause) {
        super(message, cause);
    }

    public FileDataException (Throwable cause) {
        super(cause);
    }
}
