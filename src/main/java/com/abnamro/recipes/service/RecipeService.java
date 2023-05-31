package com.abnamro.recipes.service;

import com.abnamro.recipes.exceptions.ApplicationRuntimeException;
import com.abnamro.recipes.exceptions.DataNotFoundException;
import com.abnamro.recipes.model.Ingredient;
import com.abnamro.recipes.model.Recipe;
import com.abnamro.recipes.model.request.RecipeRequest;
import com.abnamro.recipes.model.request.SearchRequest;
import com.abnamro.recipes.model.response.GenericResponse;
import com.abnamro.recipes.repository.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final IngredientService ingredientService;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientService ingredientService) {
        this.recipeRepository = recipeRepository;
        this.ingredientService = ingredientService;
    }

    public Recipe saveRecipe(RecipeRequest recipeRequest, Integer id) {

        Recipe recipe = new Recipe();
        try {
            if (id != null) {
                Recipe updateRecipe = recipeRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Recipe not Found"));
                if (updateRecipe != null) {
                    recipe.setId(updateRecipe.getId());
                }
            }
            Set<Ingredient> ingredients = Optional.ofNullable(recipeRequest.getIngredientIds())
                    .map(ingredientService::getByIds)
                    .orElse(null);
            recipe.setName(recipeRequest.getName().toLowerCase());
            recipe.setInstructions(recipeRequest.getInstructions().toLowerCase());
            recipe.setType(recipeRequest.getType());
            recipe.setNumberOfServings(recipeRequest.getNumberOfServings());
            recipe.setRecipeIngredients(ingredients);
           return recipeRepository.save(recipe);
        } catch (DataNotFoundException e) {
            log.error("Exception occurred while finding the recipe {}", e.getMessage());
            throw new DataNotFoundException("Recipe Not Found");
        } catch (Exception e) {
            log.error("Exception occurred while saving the recipe {}", e.getMessage());
            throw new ApplicationRuntimeException("Unable to save the recipe");
        }
    }

    public Recipe getRecipeById(int id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Recipe not Found"));
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public GenericResponse deleteRecipe(int id) {
        if (!recipeRepository.existsById(id)) {
            throw new DataNotFoundException("Recipe not Found");
        }
        try {
            recipeRepository.deleteById(id);
            return new GenericResponse("Record deleted successfully");
        } catch (Exception e) {
            log.error("Exception occurred while deleting recipe {}", e.getMessage());
            throw new ApplicationRuntimeException("Unable to delete the recipe");
        }
    }

    public List<Recipe> searchRecipe(SearchRequest searchRequest) {
        if(searchRequest.getExcludeIngredients().isEmpty() && searchRequest.getIncludeIngredients().isEmpty()) {
            return recipeRepository.findByTypeOrInstOrServings(searchRequest.getType(), searchRequest.getInstructions().toLowerCase(), searchRequest.getNumberOfServings());
        } else if (!searchRequest.getExcludeIngredients().isEmpty() && searchRequest.getIncludeIngredients().isEmpty()) {
            return recipeRepository.findByTypeOrInstOrServingsWithExclude(searchRequest.getType(), searchRequest.getInstructions(), searchRequest.getNumberOfServings(), convertToLowerCase(searchRequest.getExcludeIngredients()));
        } else {
            return recipeRepository.findByTypeOrInstOrServingsWithAll(searchRequest.getType(), searchRequest.getInstructions(), searchRequest.getNumberOfServings(), convertToLowerCase(searchRequest.getIncludeIngredients()), convertToLowerCase(searchRequest.getExcludeIngredients()));
        }
    }

    private List<String> convertToLowerCase(List<String> ingredients) {
        return ingredients.stream().map(s -> s.toLowerCase()).collect(Collectors.toList());
    }

}
