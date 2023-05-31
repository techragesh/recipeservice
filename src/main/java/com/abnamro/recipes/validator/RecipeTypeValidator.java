package com.abnamro.recipes.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom Validator class for Recipe Type
 *
 * @author Ragesh Sharma
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = RecipeTypeValidatorConstraint.class)
public @interface RecipeTypeValidator {

    Class<? extends Enum<?>> enumClass();

    String message() default "Recipe Type is Invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
