package com.abnamro.recipes.exceptions;

public class DataNotFoundException extends RuntimeException{

    /**
     * Instantiates a new DataNotFoundException.
     */
    public DataNotFoundException() {
        // Default Constructor
    }

    /**
     * Instantiates a new DataNotFoundException.
     *
     * @param message - String
     */
    public DataNotFoundException(String message) {
        super(message);
    }

    /**
     * Instantiates a new DataNotFoundException.
     *
     * @param message - String
     * @param cause - Throwable
     */
    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
