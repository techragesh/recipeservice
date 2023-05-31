package com.abnamro.recipes.service;

import com.abnamro.recipes.exceptions.ApplicationRuntimeException;
import com.abnamro.recipes.exceptions.DataNotFoundException;
import com.abnamro.recipes.mapper.RecipeMapper;
import com.abnamro.recipes.model.Ingredient;
import com.abnamro.recipes.model.Recipe;
import com.abnamro.recipes.model.request.RecipeRequest;
import com.abnamro.recipes.model.request.SearchRequest;
import com.abnamro.recipes.model.response.GenericResponse;
import com.abnamro.recipes.model.response.RecipeResponse;
import com.abnamro.recipes.repository.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for handling Recipe Transactions
 *
 * @author Ragesh Sharma
 */
@Service
@Transactional
@Slf4j
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final IngredientService ingredientService;

    private final RecipeMapper recipeMapper;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientService ingredientService, RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.ingredientService = ingredientService;
        this.recipeMapper = recipeMapper;
    }

    public RecipeResponse saveRecipe(RecipeRequest recipeRequest, Integer id) {
        log.debug("save recipe request {}", recipeRequest);
        Recipe recipe = new Recipe();
        try {
            if (id != null) {
                Recipe updateRecipe = recipeRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Recipe not Found"));
                if (updateRecipe != null) {
                    recipe.setId(updateRecipe.getId());
                }
            }
            Set<Ingredient> ingredients = Optional.ofNullable(recipeRequest.getIngredientIds())
                    .map(ingredientService::getByIngIds)
                    .orElse(null);
            recipe.setName(recipeRequest.getName().toLowerCase());
            recipe.setInstructions(recipeRequest.getInstructions().toLowerCase());
            recipe.setType(recipeRequest.getType());
            recipe.setNumberOfServings(recipeRequest.getNumberOfServings());
            recipe.setRecipeIngredients(ingredients);
            log.debug("save recipe final request after mapping {}", recipe);
           return recipeMapper.convertEntityToDTO(recipeRepository.save(recipe));
        } catch (DataNotFoundException e) {
            log.error("Exception occurred while finding the recipe {}", e.getMessage());
            throw new DataNotFoundException("Recipe Not Found");
        } catch (Exception e) {
            log.error("Exception occurred while saving the recipe {}", e.getMessage());
            throw new ApplicationRuntimeException("Unable to save the recipe");
        }
    }

    public RecipeResponse getRecipeById(int id) {
        return recipeMapper.convertEntityToDTO(recipeRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Recipe not Found")));
    }

    public List<RecipeResponse> getAllRecipes() {
        return recipeMapper.convertEntityToDTOList(recipeRepository.findAll());
    }

    public GenericResponse deleteRecipe(int id) {
        if (!recipeRepository.existsById(id)) {
            log.debug("recipe not found for the id {}", id);
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

    public List<RecipeResponse> searchRecipe(SearchRequest searchRequest) {
        if(searchRequest.getExcludeIngredients().isEmpty() && searchRequest.getIncludeIngredients().isEmpty()) {
            return recipeMapper.convertEntityToDTOList(recipeRepository.findByTypeOrInstOrServings(searchRequest.getType(), searchRequest.getInstructions().toLowerCase(), searchRequest.getNumberOfServings()).orElseThrow(() -> new DataNotFoundException("Recipe not Found")));
        } else if (!searchRequest.getExcludeIngredients().isEmpty() && searchRequest.getIncludeIngredients().isEmpty()) {
            return recipeMapper.convertEntityToDTOList(recipeRepository.findByTypeOrInstOrServingsWithExclude(searchRequest.getType(), searchRequest.getInstructions(), searchRequest.getNumberOfServings(), convertToLowerCase(searchRequest.getExcludeIngredients())).orElseThrow(() -> new DataNotFoundException("Recipe not Found")));
        } else {
            return recipeMapper.convertEntityToDTOList(recipeRepository.findByTypeOrInstOrServingsWithAll(searchRequest.getType(), searchRequest.getInstructions(), searchRequest.getNumberOfServings(), convertToLowerCase(searchRequest.getIncludeIngredients()), convertToLowerCase(searchRequest.getExcludeIngredients())).orElseThrow(() -> new DataNotFoundException("Recipe not Found")));
        }
    }

    private List<String> convertToLowerCase(List<String> ingredients) {
        return ingredients.stream().map(s -> s.toLowerCase()).collect(Collectors.toList());
    }

}
