package com.abnamro.recipes.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Response class for the recipe
 *
 * @author Ragesh Sharma
 */
@Data
@NoArgsConstructor
public class RecipeResponse {

    @ApiModelProperty(notes = "Id of the recipe", example = "1")
    private int id;

    @ApiModelProperty(notes = "Name of the recipe", example = "Pizza")
    private String name;

    @ApiModelProperty(notes = "Type of the recipe", example = "VEG")
    private String type;

    @ApiModelProperty(notes = "Number of servings", example = "1")
    private int numberOfServings;

    @JsonIgnoreProperties("ingredients")
    private Set<IngredientResponse> ingredients;

    @ApiModelProperty(notes = "Instructions of the recipe", example = "Prepare pizza dough and base, Chop vegetables and spread the sauce and veggies on the base and bake it.")
    private String instructions;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updatedDateTime;
}
