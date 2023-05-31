package com.abnamro.recipes.mapper;


import com.abnamro.recipes.model.Ingredient;
import com.abnamro.recipes.model.Recipe;
import com.abnamro.recipes.model.response.RecipeResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper class to convert entity to dto for recipe
 *
 * @author Ragesh Sharma
 */
@Component
public class RecipeMapper {

    public RecipeResponse convertEntityToDTO(Recipe recipe) {

        Set<Ingredient> ingredientSet = recipe.getRecipeIngredients();

        RecipeResponse recipeResponse = new RecipeResponse();
        recipeResponse.setId(recipe.getId());
        recipeResponse.setName(recipe.getName());
        recipeResponse.setInstructions(recipe.getInstructions());
        recipeResponse.setIngredients(ingredientSet!=null ? ingredientSet.stream().map(ingredient -> new IngredientMapper().convertEntityToDTO(ingredient)).collect(Collectors.toSet()): null);
        recipeResponse.setType(recipe.getType());
        recipeResponse.setNumberOfServings(recipe.getNumberOfServings());
        recipeResponse.setCreatedDateTime(recipe.getCreatedDateTime());
        recipeResponse.setUpdatedDateTime(recipe.getModifiedDateTime());
        return recipeResponse;
    }

    public List<RecipeResponse> convertEntityToDTOList(List<Recipe> recipes) {
        if(recipes != null && !recipes.isEmpty()) {
            return recipes.stream().map(recipe -> convertEntityToDTO(recipe)).collect(Collectors.toList());
        }
        return null;
    }

}
