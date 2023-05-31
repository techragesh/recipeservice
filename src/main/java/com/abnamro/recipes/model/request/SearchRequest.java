package com.abnamro.recipes.model.request;

import com.abnamro.recipes.model.RecipeType;
import com.abnamro.recipes.util.StringListConverter;
import com.abnamro.recipes.util.ValidationUtil;
import com.abnamro.recipes.validator.RecipeTypeValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class SearchRequest {

    @ApiModelProperty(notes = "Type of the recipe", example = "VEG or NONVEG")
    @RecipeTypeValidator(enumClass = RecipeType.class, message = "Recipe Type is Invalid")
    private String type;

    @Positive(message = "NumberOfServings input is Invalid")
    @ApiModelProperty(notes = "Number of servings per recipe", example = "4")
    private int numberOfServings;

    @Size(max =255, message = "Recipe Instructions can be 255 characters long")
    @Pattern(regexp = ValidationUtil.PATTERN_INSTRUCTIONS, message = "Please provide a valid instructions. Allowed only this special characters: \\ / - + . , ? ! * ( ) ; ")
    @ApiModelProperty(notes = "Instructions to search the recipe", example = "Prepare pizza dough and base, Chop vegetables and spread the sauce and veggies on the base and bake it.")
    private String instructions;

    @Size(max = 100, message = "Exclude Ingredient Name can be 100 characters long")
    @ApiModelProperty(notes = "Name of the ingredient", example = "Potato")
    @Convert(converter = StringListConverter.class)
    private List<String> excludeIngredients;

    @Size(max = 100, message = "Include Ingredient Name can be 100 characters long")
    @ApiModelProperty(notes = "Name of the ingredient", example = "Tomato")
    @Convert(converter = StringListConverter.class)
    private List<String> includeIngredients;
}
