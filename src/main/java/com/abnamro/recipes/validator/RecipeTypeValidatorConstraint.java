package com.abnamro.recipes.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
/**
 * Custom Validator Constraint class for Recipe Type
 *
 * @author Ragesh Sharma
 */
public class RecipeTypeValidatorConstraint implements ConstraintValidator<RecipeTypeValidator, String> {

    private List<String> recipeTypes;

    @Override
    public void initialize(RecipeTypeValidator constraintAnnotation) {
        recipeTypes = new ArrayList<>();
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();
        Enum[] enumValArr = enumClass.getEnumConstants();
        for (Enum enumVal : enumValArr) {
            recipeTypes.add(enumVal.toString().toUpperCase());
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return recipeTypes.contains(value.toUpperCase());
    }
}
