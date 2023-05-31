package com.abnamro.recipes.model.request;

import com.abnamro.recipes.model.RecipeType;
import com.abnamro.recipes.util.ValidationUtil;
import com.abnamro.recipes.validator.RecipeTypeValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequest {

    @NotBlank(message = "Recipe Name is required")
    @Size(max = 100, message = "Recipe Name can be 100 characters long")
    @Pattern(regexp = ValidationUtil.PATTERN_NAME, message = " Recipe name should contain only letters and following characters ',.- and space")
    @ApiModelProperty(notes = "Name of the recipe", example = "Pizza")
    private String name;

    @ApiModelProperty(notes = "Type of the recipe", example = "VEG or NONVEG")
    @RecipeTypeValidator(enumClass = RecipeType.class, message = "Recipe Type is Invalid")
    private String type;

    @NotNull(message = "NumberOfServings input is Invalid")
    @Positive(message = "NumberOfServings input is Invalid")
    @ApiModelProperty(notes = "Number of servings per recipe", example = "4")
    private int numberOfServings;

    @ApiModelProperty(notes = "Ids of the ingredients needed to make the recipe", example = "[1,2]")
    private List<Integer> ingredientIds;

    @NotBlank(message = "Instructions is required")
    @Size(max =255, message = "Recipe Instructions can be 255 characters long")
    @Pattern(regexp = ValidationUtil.PATTERN_INSTRUCTIONS, message = "Please provide a valid instructions. Allowed only this special characters: \\ / - + . , ? ! * ( ) ; ")
    @ApiModelProperty(notes = "Instructions to create the recipe", example = "Prepare pizza dough and base, Chop vegetables and spread the sauce and veggies on the base and bake it.")
    private String instructions;

}
