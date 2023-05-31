package com.abnamro.recipes.service;

import com.abnamro.recipes.RecipeTestData;
import com.abnamro.recipes.exceptions.ApplicationRuntimeException;
import com.abnamro.recipes.exceptions.DataNotFoundException;
import com.abnamro.recipes.mapper.RecipeMapper;
import com.abnamro.recipes.model.Recipe;
import com.abnamro.recipes.model.request.RecipeRequest;
import com.abnamro.recipes.model.request.SearchRequest;
import com.abnamro.recipes.model.response.GenericResponse;
import com.abnamro.recipes.model.response.RecipeResponse;
import com.abnamro.recipes.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private RecipeMapper mapper;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    public void test_saveRecipe_success() {

        RecipeRequest request = RecipeTestData.createRecipeRequest();

        Recipe response = RecipeTestData.formRecipe();
        RecipeResponse resp = RecipeTestData.formRecipeResponse();

        when(mapper.convertEntityToDTO(any(Recipe.class))).thenReturn(resp);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(response);

        RecipeResponse recipe = recipeService.saveRecipe(request, null);

        assertThat(recipe.getId()).isSameAs(response.getId());
    }

    @Test
    public void test_saveRecipe_failed() {
        RecipeRequest request = RecipeTestData.createRecipeRequest();

        when(recipeRepository.save(any(Recipe.class))).thenThrow(new ApplicationRuntimeException("Unable to save the recipe"));

        ApplicationRuntimeException exception = assertThrows(ApplicationRuntimeException.class, () -> {
            recipeService.saveRecipe(request, null);
        });
        assertEquals("Unable to save the recipe", exception.getMessage());
    }

    @Test
    public void test_updateRecipe_success() {
        Recipe response = RecipeTestData.formRecipe();
        response.setId(11);

        RecipeRequest request = new RecipeRequest("pasta", "OTHER", 4, null, "instructions");
        RecipeResponse res = RecipeTestData.formRecipeResponse();
        res.setId(11);

        when(mapper.convertEntityToDTO(any())).thenReturn(res);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(response);
        when(recipeRepository.findById(anyInt())).thenReturn(Optional.of(response));

        RecipeResponse recipe = recipeService.saveRecipe(request, 11);

        assertThat(recipe.getId()).isSameAs(response.getId());
    }

    @Test
    public void test_updateRecipe_dataNotFound() {

        RecipeRequest request = RecipeTestData.createRecipeRequest();

        when(recipeRepository.findById(anyInt())).thenThrow(new DataNotFoundException("Recipe Not Found"));

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
            recipeService.saveRecipe(request, 22);
        });

        assertEquals("Recipe Not Found", exception.getMessage());
    }

    @Test
    public void test_getRecipe_byId() {

        Recipe response = new Recipe();
        response.setName("Name");
        response.setInstructions("instructions");
        response.setNumberOfServings(4);
        response.setId(1);

        when(mapper.convertEntityToDTO(any())).thenReturn(RecipeTestData.formRecipeResponse());
        when(recipeRepository.findById(anyInt())).thenReturn(Optional.of(response));


        RecipeResponse result = recipeService.getRecipeById(1);
        assertEquals(response.getId(), result.getId());
    }

    @Test
    public void test_getRecipe_dataNotFound() {
        when(recipeRepository.findById(anyInt())).thenThrow(new DataNotFoundException("Recipe not Found"));

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
            recipeService.getRecipeById(1);
        });
        assertEquals("Recipe not Found", exception.getMessage());
    }

    @Test
    public void test_getAllRecipe() {

        when(mapper.convertEntityToDTOList(any())).thenReturn(RecipeTestData.getRecipesResponse());
        when(recipeRepository.findAll()).thenReturn(RecipeTestData.getRecipes());

        List<RecipeResponse> result = recipeService.getAllRecipes();
        assertEquals(2, result.size());
    }

    @Test
    public void test_deleteRecipe_success() {

        when(recipeRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(recipeRepository).deleteById(anyInt());

        GenericResponse response = recipeService.deleteRecipe(1);
        assertEquals("Record deleted successfully", response.getMessage());
    }

    @Test
    public void test_deleteRecipe_dataNotFound() {
        when(recipeRepository.existsById(anyInt())).thenReturn(false);

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
            recipeService.deleteRecipe(1);
        });
        assertEquals("Recipe not Found", exception.getMessage());
    }

    @Test
    public void test_deleteRecipe_Failed() {

        when(recipeRepository.existsById(anyInt())).thenReturn(true);
        doThrow(new ApplicationRuntimeException("Unable to delete the recipe")).when(recipeRepository).deleteById(anyInt());

        ApplicationRuntimeException exception = assertThrows(ApplicationRuntimeException.class, () -> {
            recipeService.deleteRecipe(1);
        });
        assertEquals("Unable to delete the recipe", exception.getMessage());
    }

    @Test
    public void test_searchRecipe_1() {

        when(mapper.convertEntityToDTOList(any())).thenReturn(RecipeTestData.getRecipesResponse());
        when(recipeRepository.findByTypeOrInstOrServings(anyString(), anyString(), anyInt())).thenReturn(Optional.of(RecipeTestData.getRecipes()));

        List<RecipeResponse> result = recipeService.searchRecipe(RecipeTestData.createSearchRequest());
        assertEquals(2, result.size());
    }

    @Test
    public void test_searchRecipe_1_not_found() {

        when(recipeRepository.findByTypeOrInstOrServings(anyString(), anyString(), anyInt())).thenThrow(new DataNotFoundException("Recipe not Found"));

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
            recipeService.searchRecipe(RecipeTestData.createSearchRequest());
        });
        assertEquals("Recipe not Found", exception.getMessage());
    }

    @Test
    public void test_searchRecipe_2() {

        SearchRequest searchRequest = RecipeTestData.createSearchRequest();
        searchRequest.setExcludeIngredients(Arrays.asList("Garlic"));

        when(mapper.convertEntityToDTOList(any())).thenReturn(RecipeTestData.getRecipesResponse());
        when(recipeRepository.findByTypeOrInstOrServingsWithExclude(anyString(), anyString(), anyInt(), any())).thenReturn(Optional.of(RecipeTestData.getRecipes()));

        List<RecipeResponse> result = recipeService.searchRecipe(searchRequest);
        assertEquals(2, result.size());

    }

    @Test
    public void test_searchRecipe_3() {

        SearchRequest searchRequest = RecipeTestData.createSearchRequest();
        searchRequest.setExcludeIngredients(Arrays.asList("Garlic"));
        searchRequest.setIncludeIngredients(Arrays.asList("Onion"));

        when(mapper.convertEntityToDTOList(any())).thenReturn(RecipeTestData.getRecipesResponse());
        when(recipeRepository.findByTypeOrInstOrServingsWithAll(anyString(), anyString(), anyInt(), any(), any())).thenReturn(Optional.of(RecipeTestData.getRecipes()));

        List<RecipeResponse> result = recipeService.searchRecipe(searchRequest);
        assertEquals(2, result.size());
    }
}
