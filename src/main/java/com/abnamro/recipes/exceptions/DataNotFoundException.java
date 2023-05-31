package com.abnamro.recipes.exceptions;

/**
 * Custom exception class for Data Not Found
 *
 * @author Ragesh Sharma
 */
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
}
