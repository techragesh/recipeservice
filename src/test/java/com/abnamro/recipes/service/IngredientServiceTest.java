package com.abnamro.recipes.service;

import com.abnamro.recipes.RecipeTestData;
import com.abnamro.recipes.exceptions.ApplicationRuntimeException;
import com.abnamro.recipes.exceptions.DataNotFoundException;
import com.abnamro.recipes.model.Ingredient;
import com.abnamro.recipes.model.request.IngredientRequest;
import com.abnamro.recipes.model.response.GenericResponse;
import com.abnamro.recipes.repository.IngredientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private IngredientService ingredientService;

    @Test
    public void test_saveIngredient_success() {
        IngredientRequest request = RecipeTestData.createIngredientRequest();

        Ingredient response = RecipeTestData.formIngredient();

        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(response);
        Ingredient result = ingredientService.saveIngredient(request);
        assertThat(response.getId()).isSameAs(result.getId());
    }

    @Test
    public void test_saveIngredient_failed() {

        when(ingredientRepository.save(any(Ingredient.class))).thenThrow(new ApplicationRuntimeException("Unable to save the ingredient"));

        ApplicationRuntimeException exception = assertThrows(ApplicationRuntimeException.class, () -> {
            ingredientService.saveIngredient(RecipeTestData.createIngredientRequest());
        });
        assertEquals("Unable to save the ingredient", exception.getMessage());
    }

    @Test
    public void test_getIngredient_byId() {

        Ingredient response = RecipeTestData.formIngredient();

        when(ingredientRepository.findById(anyInt())).thenReturn(Optional.of(response));


        Ingredient result = ingredientService.getIngredientById(1);
        assertEquals(response.getId(), result.getId());
    }

    @Test
    public void test_getIngredient_byIds() {

        List<Integer> ingredientIds = Arrays.asList(1);

        Ingredient response = RecipeTestData.formIngredient();

        when(ingredientRepository.findById(anyInt())).thenReturn(Optional.of(response));


        Set<Ingredient> result = ingredientService.getByIds(ingredientIds);
        assertNotNull(result);
    }

    @Test
    public void test_getAllIngredient() {

        when(ingredientRepository.findAll()).thenReturn(RecipeTestData.getIngredients());

        List<Ingredient> result = ingredientService.getAllIngredients();
        assertEquals(2, result.size());
    }

    @Test
    public void test_deleteIngredient_success() {

        when(ingredientRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(ingredientRepository).deleteById(anyInt());

        GenericResponse response = ingredientService.delete(1);
        assertEquals("Record deleted successfully", response.getMessage());
    }

    @Test
    public void test_deleteIngredient_dataNotFound() {
        when(ingredientRepository.existsById(anyInt())).thenReturn(false);

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
            ingredientService.delete(1);
        });
        assertEquals("Ingredient not Found", exception.getMessage());
    }

    @Test
    public void test_deleteIngredient_Failed() {

        when(ingredientRepository.existsById(anyInt())).thenReturn(true);
        doThrow(new ApplicationRuntimeException("Unable to delete the ingredient")).when(ingredientRepository).deleteById(anyInt());

        ApplicationRuntimeException exception = assertThrows(ApplicationRuntimeException.class, () -> {
            ingredientService.delete(1);
        });
        assertEquals("Unable to delete the ingredient", exception.getMessage());
    }

}
