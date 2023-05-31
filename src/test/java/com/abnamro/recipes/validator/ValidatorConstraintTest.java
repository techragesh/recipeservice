package com.abnamro.recipes.validator;

import com.abnamro.recipes.model.request.IngredientRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidatorConstraintTest {

    private static Validator validator;

    @BeforeAll
    public static void setupValidatorInstance() {
        validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
    }

    @Test
    public void whenNotBlankName_thenNoConstraintViolations() {
        IngredientRequest request = new IngredientRequest("pasta");

        Set<ConstraintViolation<IngredientRequest>> violations = validator.validate(request);

        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    public void whenBlankName_thenOneConstraintViolation() {
        IngredientRequest request = new IngredientRequest(null);

        Set<ConstraintViolation<IngredientRequest>> violations = validator.validate(request);
        String collect = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        assertEquals(collect, "Ingredient name is required");
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void whenEmptyName_thenOneConstraintViolation() {
        IngredientRequest request = new IngredientRequest(null);

        Set<ConstraintViolation<IngredientRequest>> violations = validator.validate(request);
        String collect = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        assertEquals(collect, "Ingredient name is required");
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void whenNameDoesNotFitPattern_thenOneConstraintViolation() {
        IngredientRequest request = new IngredientRequest("-.1!@$!#@");

        Set<ConstraintViolation<IngredientRequest>> violations = validator.validate(request);
        String collect = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        assertEquals(collect, "Ingredient name should contain only letters and following characters ',.- and space");
        assertThat(violations.size()).isEqualTo(1);
    }
}
