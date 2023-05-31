package com.abnamro.recipes.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Response class for the generic message
 *
 * @author Ragesh Sharma
 */
@Data
@AllArgsConstructor
public class GenericResponse {

    private String message;
}
