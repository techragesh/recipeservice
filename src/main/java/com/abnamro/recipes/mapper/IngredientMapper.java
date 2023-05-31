package com.abnamro.recipes.mapper;

import com.abnamro.recipes.model.Ingredient;
import com.abnamro.recipes.model.response.IngredientResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class to convert entity to dto for ingredient
 *
 * @author Ragesh Sharma
 */
@Component
public class IngredientMapper {

    public IngredientResponse convertEntityToDTO(Ingredient ingredient) {
        IngredientResponse ingredientResponse = new IngredientResponse();
        ingredientResponse.setName(ingredient.getName());
        ingredientResponse.setId(ingredient.getId());
        ingredientResponse.setCreatedDateTime(ingredient.getCreatedDateTime());
        ingredientResponse.setUpdatedDateTime(ingredient.getModifiedDateTime());
        return ingredientResponse;
    }

    public List<IngredientResponse> convertEntityToDTOList(List<Ingredient> ingredients) {
        if(ingredients != null && !ingredients.isEmpty()) {
            return ingredients.stream().map(ingredient -> convertEntityToDTO(ingredient)).collect(Collectors.toList());
        }
        return null;
    }

}
