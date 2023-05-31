package com.abnamro.recipes.exceptions;

import com.abnamro.recipes.controller.IngredientController;
import com.abnamro.recipes.controller.RecipeController;
import com.abnamro.recipes.model.response.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice(assignableTypes = { RecipeController.class, IngredientController.class})
public class ApplicationExceptionHandler {

    /**
     * For ApplicationRuntimeException
     *
     * @param ex exception
     * @return ErrorResponse Object
     */
    @ExceptionHandler(ApplicationRuntimeException.class)
    public ResponseEntity<GenericResponse> handleApplicationException(ApplicationRuntimeException ex) {
        log.error("Application Runtime Exception {}", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(formErrorResponse("TECHNICAL_ERROR"));
    }

    /**
     * For ConstraintViolationException
     *
     * @param ex exception
     * @return ErrorResponse Object
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GenericResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Invalid Input {}", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(formErrorResponse("BAD_REQUEST"));
    }

    /**
     * For HttpMessageNotReadableException
     *
     * @param ex exception
     * @return ErrorResponse Object
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GenericResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("Invalid Input {}", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(formErrorResponse("BAD_REQUEST"));
    }

    /**
     * For DataNotFoundException
     *
     * @param ex exception
     * @return ErrorResponse Object
     */
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<GenericResponse> handleDataNotFoundException(DataNotFoundException ex) {
        log.error("Data Not Found {}", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(formErrorResponse("DATA_NOT_FOUND"));
    }

    /**
     * For ConstraintViolationException
     *
     * @param ex exception
     * @return ErrorResponse Object
     */
    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
    public ResponseEntity<GenericResponse> handleConstraintViolationException(org.hibernate.exception.ConstraintViolationException ex) {
        log.error("Constraint Exception {}", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(formErrorResponse("UNABLE_TO_DELETE_RECORD"));
    }


    private GenericResponse formErrorResponse(String message) {
        return new GenericResponse(message);
    }
}
