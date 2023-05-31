package com.abnamro.recipes.controller;

import com.abnamro.recipes.RecipeTestData;
import com.abnamro.recipes.exceptions.ApplicationRuntimeException;
import com.abnamro.recipes.exceptions.DataNotFoundException;
import com.abnamro.recipes.model.request.IngredientRequest;
import com.abnamro.recipes.model.response.GenericResponse;
import com.abnamro.recipes.model.response.IngredientResponse;
import com.abnamro.recipes.service.IngredientService;
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
public class IngredientControllerTest {

    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private IngredientController ingredientController;

    @Test
    public void test_saveIngredient_success() {

        IngredientRequest request = RecipeTestData.createIngredientRequest();

        IngredientResponse response = RecipeTestData.formIngredientResponse();

        when(ingredientService.saveIngredient(any(IngredientRequest.class))).thenReturn(response);

        ResponseEntity<IngredientResponse> result = ingredientController.saveIngredient(request);

        assertThat(result).isNotNull();
        assertEquals(201, result.getStatusCodeValue());
    }

    @Test
    public void test_saveIngredient_failed() {
        IngredientRequest request = RecipeTestData.createIngredientRequest();

        when(ingredientService.saveIngredient(any(IngredientRequest.class))).thenThrow(new ApplicationRuntimeException("Unable to save the ingredient"));

        ApplicationRuntimeException exception = assertThrows(ApplicationRuntimeException.class, () -> {
            ingredientController.saveIngredient(request);
        });
        assertEquals("Unable to save the ingredient", exception.getMessage());
    }

    @Test
    public void test_getIngredientById_success() {

        IngredientResponse response = RecipeTestData.formIngredientResponse();

        when(ingredientService.getIngredientById(anyInt())).thenReturn(response);

        ResponseEntity<IngredientResponse> result = ingredientController.getIngredientById(1);

        assertThat(result).isNotNull();
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void test_getIngredientById_dataNotFound() {

        when(ingredientService.getIngredientById(anyInt())).thenThrow(new DataNotFoundException("Ingredient not Found"));

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
            ingredientController.getIngredientById(1);
        });

        assertEquals("Ingredient not Found", exception.getMessage());
    }

    @Test
    public void test_getIngredientAll_success() {

        List<IngredientResponse> ingredients = RecipeTestData.getIngredientsResponse();

        when(ingredientService.getAllIngredients()).thenReturn(ingredients);

        ResponseEntity<List<IngredientResponse>> result = ingredientController.getAllIngredient();

        assertThat(result).isNotNull();
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void test_deleteIngredient_success() {

        when(ingredientService.delete(anyInt())).thenReturn(new GenericResponse("Record deleted successfully"));
        ResponseEntity<GenericResponse> result = ingredientController.deleteIngredient(1);
        assertThat(result).isNotNull();
        assertEquals(200, result.getStatusCodeValue());
    }
}
