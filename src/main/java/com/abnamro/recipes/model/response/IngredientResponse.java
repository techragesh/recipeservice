package com.abnamro.recipes.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response class for the ingredient
 *
 * @author Ragesh Sharma
 */
@Data
@NoArgsConstructor
public class IngredientResponse {

    @ApiModelProperty(notes = "Id of the Ingredient", example = "1")
    private int id;
    @ApiModelProperty(notes = "Name of the Ingredient", example = "Tomato")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updatedDateTime;
}
