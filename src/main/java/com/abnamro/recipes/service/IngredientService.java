package com.abnamro.recipes.service;

import com.abnamro.recipes.exceptions.ApplicationRuntimeException;
import com.abnamro.recipes.exceptions.DataNotFoundException;
import com.abnamro.recipes.model.Ingredient;
import com.abnamro.recipes.model.request.IngredientRequest;
import com.abnamro.recipes.model.response.GenericResponse;
import com.abnamro.recipes.repository.IngredientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient saveIngredient(IngredientRequest ingredientRequest) {
        try {
            Ingredient ingredient = new Ingredient();
            ingredient.setIngredient(ingredientRequest.getName().toLowerCase());
            Ingredient createdIngredient = ingredientRepository.save(ingredient);
            return createdIngredient;
        } catch(Exception e) {
            log.error("Exception occurred while saving the ingredient {}", e.getMessage());
            throw new ApplicationRuntimeException("Unable to save the ingredient");
        }
    }

    public Set<Ingredient> getByIds(List<Integer> ingredientIds) {
        return ingredientIds.stream()
                .map(this::getIngredientById)
                .collect(Collectors.toSet());
    }

    public Ingredient getIngredientById(int id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Ingredient not Found"));
    }

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public GenericResponse delete(int id) {
        if (!ingredientRepository.existsById(id)) {
            throw new DataNotFoundException("Ingredient not Found");
        }
        try {
            ingredientRepository.deleteById(id);
            return new GenericResponse("Record deleted successfully");
        } catch (Exception e) {
            log.error("Exception occurred while deleting ingredient {}", e.getMessage());
            throw new ApplicationRuntimeException("Unable to delete the ingredient");
        }
    }

}
