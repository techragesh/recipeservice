package com.abnamro.recipes.util;

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
