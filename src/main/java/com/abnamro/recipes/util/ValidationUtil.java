package com.abnamro.recipes.util;

/**
 * Utility class for text validation
 *
 * @author Ragesh Sharma
 */
public class ValidationUtil {

    /**
     * Pattern for name of the recipes
     */
    public static final String PATTERN_NAME = "^(?:\\p{L}\\p{M}*|[',. \\-]|\\s)*$";

    /**
     * Pattern for instructions
     */
    public static final String PATTERN_INSTRUCTIONS = "^(?:\\p{L}\\p{M}*|[0-9]*|[\\/\\-+.,?!*();\"]|\\s)*$";
}
