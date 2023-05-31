package com.abnamro.recipes;

import com.abnamro.recipes.model.Ingredient;
import com.abnamro.recipes.model.Recipe;
import com.abnamro.recipes.model.request.IngredientRequest;
import com.abnamro.recipes.model.request.RecipeRequest;
import com.abnamro.recipes.model.request.SearchRequest;
import com.abnamro.recipes.model.response.GenericResponse;
import com.abnamro.recipes.model.response.IngredientResponse;
import com.abnamro.recipes.model.response.RecipeResponse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecipeTestData {

    public static RecipeRequest createRecipeRequest() {
        return new RecipeRequest("pizza", "VEG", 4, null, "instructions");
    }

    public static IngredientRequest createIngredientRequest() {
        return new IngredientRequest("potato");
    }

    public static Recipe formRecipe() {
        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setName("pizza");
        recipe.setType("VEG");
        recipe.setNumberOfServings(4);
        recipe.setInstructions("instructions");
        return recipe;
    }

    public static RecipeResponse formRecipeResponse() {
        RecipeResponse recipe = new RecipeResponse();
        recipe.setId(1);
        recipe.setName("pizza");
        recipe.setType("VEG");
        recipe.setNumberOfServings(4);
        recipe.setInstructions("instructions");
        return recipe;
    }

    public static Ingredient formIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1);
        ingredient.setName("potato");
        return ingredient;
    }

    public static IngredientResponse formIngredientResponse() {
        IngredientResponse ingredient = new IngredientResponse();
        ingredient.setId(1);
        ingredient.setName("potato");
        return ingredient;
    }

    public static List<Recipe> getRecipes() {
        Recipe recipe1 = new Recipe();
        recipe1.setId(1);
        recipe1.setName("pizza");

        Recipe recipe2 = new Recipe();
        recipe2.setId(2);
        recipe2.setName("pasta");
        return Arrays.asList(recipe1, recipe2);
    }

    public static List<RecipeResponse> getRecipesResponse() {
        RecipeResponse recipe1 = new RecipeResponse();
        recipe1.setId(1);
        recipe1.setName("pizza");

        RecipeResponse recipe2 = new RecipeResponse();
        recipe2.setId(2);
        recipe2.setName("pasta");
        return Arrays.asList(recipe1, recipe2);
    }

    public static List<Recipe> searchRecipes() {
        Recipe recipe1 = new Recipe();
        recipe1.setId(1);
        recipe1.setName("pizza");
        recipe1.setType("VEG");
        recipe1.setInstructions("pizza");
        recipe1.setNumberOfServings(2);
        return Arrays.asList(recipe1);
    }

    public static List<Recipe> searchRecipesError() {
        Recipe recipe1 = new Recipe();
        recipe1.setId(1);
        recipe1.setName("pizza");
        recipe1.setType("VEG");
        recipe1.setInstructions("pizza");
        recipe1.setNumberOfServings(0);
        return Arrays.asList(recipe1);
    }

    public static List<RecipeResponse> searchRecipesResponse() {
        RecipeResponse recipe1 = new RecipeResponse();
        recipe1.setId(1);
        recipe1.setName("pizza");
        recipe1.setType("VEG");
        recipe1.setNumberOfServings(2);
        return Arrays.asList(recipe1);
    }

    public static List<Ingredient> getIngredients() {
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1);
        ingredient1.setName("potato");

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2);
        ingredient2.setName("cheese");
        return Arrays.asList(ingredient1, ingredient2);
    }

    public static List<IngredientResponse> getIngredientsResponse() {
        IngredientResponse ingredient1 = new IngredientResponse();
        ingredient1.setId(1);
        ingredient1.setName("potato");

        IngredientResponse ingredient2 = new IngredientResponse();
        ingredient2.setId(2);
        ingredient2.setName("cheese");
        return Arrays.asList(ingredient1, ingredient2);
    }

    public static SearchRequest createSearchRequest() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setType("VEG");
        searchRequest.setInstructions("pizza");
        searchRequest.setNumberOfServings(2);
        searchRequest.setExcludeIngredients(Collections.emptyList());
        searchRequest.setIncludeIngredients(Collections.emptyList());
        return searchRequest;
    }

    public static GenericResponse deleteMessage() {
        return new GenericResponse("Record deleted successfully");
    }

    public static GenericResponse technicalErrorMessage() {
        return new GenericResponse("TECHNICAL_ERROR");
    }

    public static GenericResponse dataNotFoundMessage() {
        return new GenericResponse("DATA_NOT_FOUND");
    }

    public static GenericResponse invalidInputMessage() {
        return new GenericResponse("BAD_REQUEST");
    }

    public static RecipeRequest createRecipeInvalidRequest() {
        return new RecipeRequest("pizza%%%", "VEG", 4, null, "instructions");
    }
}
