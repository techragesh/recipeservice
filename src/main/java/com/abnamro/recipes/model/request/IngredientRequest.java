package com.abnamro.recipes.model.request;

import com.abnamro.recipes.util.ValidationUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Request class for the ingredient
 *
 * @author Ragesh Sharma
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientRequest {

    @NotBlank(message = "Ingredient name is required")
    @Size(max = 100, message = "Ingredient Name can be 100 characters long")
    @Pattern(regexp = ValidationUtil.PATTERN_NAME, message = "Ingredient name should contain only letters and following characters ',.- and space")
    @ApiModelProperty(notes = "Name of the ingredient", example = "Potato")
    private String name;
}
