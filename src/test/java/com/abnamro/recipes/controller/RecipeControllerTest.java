package com.abnamro.recipes.controller;

import com.abnamro.recipes.RecipeTestData;
import com.abnamro.recipes.exceptions.ApplicationRuntimeException;
import com.abnamro.recipes.exceptions.DataNotFoundException;
import com.abnamro.recipes.model.Recipe;
import com.abnamro.recipes.model.request.RecipeRequest;
import com.abnamro.recipes.model.request.SearchRequest;
import com.abnamro.recipes.model.response.GenericResponse;
import com.abnamro.recipes.model.response.RecipeResponse;
import com.abnamro.recipes.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;

    @Test
    public void test_saveRecipe_success() {
        RecipeRequest request = RecipeTestData.createRecipeRequest();

        RecipeResponse response = RecipeTestData.formRecipeResponse();

        when(recipeService.saveRecipe(any(RecipeRequest.class), any())).thenReturn(response);

        ResponseEntity<RecipeResponse> result = recipeController.saveRecipe(request);

        assertThat(result).isNotNull();
        assertEquals(201, result.getStatusCodeValue());
    }


    @Test
    public void test_saveRecipe_failed() {
        RecipeRequest request = RecipeTestData.createRecipeRequest();
        request.setName("Pizza%%%");

        when(recipeService.saveRecipe(any(RecipeRequest.class), any())).thenThrow(new ApplicationRuntimeException("Unable to save the recipe"));

        ApplicationRuntimeException exception = assertThrows(ApplicationRuntimeException.class, () -> {
            recipeController.saveRecipe(request);
        });
        assertEquals("Unable to save the recipe", exception.getMessage());
    }

    @Test
    public void test_getRecipeById_success() {

        RecipeResponse recipe = RecipeTestData.formRecipeResponse();

        when(recipeService.getRecipeById(anyInt())).thenReturn(recipe);

        ResponseEntity<RecipeResponse> result = recipeController.getRecipeById(1);

        assertThat(result).isNotNull();
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void test_getRecipeById_dataNotFound() {

        when(recipeService.getRecipeById(anyInt())).thenThrow(new DataNotFoundException("Recipe not Found"));

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
            recipeController.getRecipeById(1);
        });

        assertEquals("Recipe not Found", exception.getMessage());
    }

    @Test
    public void test_getRecipeAll_success() {

        when(recipeService.getAllRecipes()).thenReturn(RecipeTestData.getRecipesResponse());

        ResponseEntity<List<RecipeResponse>> result = recipeController.getAllRecipes();

        assertThat(result).isNotNull();
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void test_updateRecipe_success() {
        RecipeRequest request = RecipeTestData.createRecipeRequest();

        RecipeResponse response = RecipeTestData.formRecipeResponse();

        when(recipeService.saveRecipe(any(RecipeRequest.class), any())).thenReturn(response);

        ResponseEntity<RecipeResponse> result = recipeController.updateRecipe(1, request);

        assertThat(result).isNotNull();
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void test_deleteRecipe_success() {

        when(recipeService.deleteRecipe(anyInt())).thenReturn(new GenericResponse("Record deleted successfully"));
        ResponseEntity<GenericResponse> result = recipeController.deleteRecipe(1);
        assertThat(result).isNotNull();
        assertEquals(200, result.getStatusCodeValue());
    }


    @Test
    public void test_searchRecipe_success() {

        SearchRequest searchRequest = RecipeTestData.createSearchRequest();

        when(recipeService.searchRecipe(any())).thenReturn(RecipeTestData.searchRecipesResponse());

        ResponseEntity<List<RecipeResponse>> result = recipeController.searchRecipe(searchRequest);

        assertThat(result).isNotNull();
        assertEquals(200, result.getStatusCodeValue());
    }
}
