package com.abnamro.recipes.exceptions;

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

    /**
     * Instantiates a new ApplicationRuntimeException.
     *
     * @param message - String
     * @param cause - Throwable
     */
    public ApplicationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
