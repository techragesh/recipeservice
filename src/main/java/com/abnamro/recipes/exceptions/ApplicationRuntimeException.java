package com.abnamro.recipes.exceptions;

/**
 * Application exception class for handling exceptions
 *
 * @author Ragesh Sharma
 */
public class ApplicationRuntimeException extends RuntimeException{

    private static final long serialVersionUID = 2687764222713082249L;

    /**
     * Instantiates a new ApplicationRuntimeException.
     */
    public ApplicationRuntimeException() {
        // Default Constructor
    }

    /**
     * Instantiates a new ApplicationRuntimeException.
     *
     * @param message - String
     */
    public ApplicationRuntimeException(String message) {
        super(message);
    }

}
