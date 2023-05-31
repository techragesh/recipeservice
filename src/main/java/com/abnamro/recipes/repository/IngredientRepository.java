package com.abnamro.recipes.repository;

import com.abnamro.recipes.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository of Ingredient
 *
 * @author Ragesh Sharma
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
}
