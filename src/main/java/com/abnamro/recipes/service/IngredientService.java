package com.abnamro.recipes.service;

import com.abnamro.recipes.exceptions.ApplicationRuntimeException;
import com.abnamro.recipes.exceptions.DataNotFoundException;
import com.abnamro.recipes.mapper.IngredientMapper;
import com.abnamro.recipes.model.Ingredient;
import com.abnamro.recipes.model.request.IngredientRequest;
import com.abnamro.recipes.model.response.GenericResponse;
import com.abnamro.recipes.model.response.IngredientResponse;
import com.abnamro.recipes.repository.IngredientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for handling Ingredient Transactions
 *
 * @author Ragesh Sharma
 */
@Service
@Transactional
@Slf4j
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    private final IngredientMapper ingredientMapper;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    public IngredientResponse saveIngredient(IngredientRequest ingredientRequest) {
        try {
            log.debug("saveIngredient request {}", ingredientRequest);
            Ingredient ingredient = new Ingredient();
            ingredient.setName(ingredientRequest.getName().toLowerCase());
            Ingredient createdIngredient = ingredientRepository.save(ingredient);
            log.debug("ingredient saved data {}", createdIngredient);
            return ingredientMapper.convertEntityToDTO(createdIngredient);
        } catch(Exception e) {
            log.error("Exception occurred while saving the ingredient {}", e.getMessage());
            throw new ApplicationRuntimeException("Unable to save the ingredient");
        }
    }

    public Set<IngredientResponse> getByIds(List<Integer> ingredientIds) {
        return ingredientIds.stream()
                .map(this::getIngredientById)
                .collect(Collectors.toSet());
    }

    public Set<Ingredient> getByIngIds(List<Integer> ingredientIds) {
        return ingredientIds.stream()
                .map(this::getById)
                .collect(Collectors.toSet());
    }

    public Ingredient getById(int id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Ingredient not Found"));
    }

    public IngredientResponse getIngredientById(int id) {
        return ingredientMapper.convertEntityToDTO(ingredientRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Ingredient not Found")));
    }

    public List<IngredientResponse> getAllIngredients() {
        return ingredientMapper.convertEntityToDTOList(ingredientRepository.findAll());
    }

    public GenericResponse delete(int id) {
        if (!ingredientRepository.existsById(id)) {
            log.debug("ingredient not found for the id {}", id);
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
