package com.abnamro.recipes.repository;

import com.abnamro.recipes.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

        @Query(nativeQuery=true, value = "SELECT * FROM RECIPES r WHERE r.type = :type and r.instructions LIKE CONCAT('%',:instructions,'%') and r.number_of_servings >= :numberOfServings")
        List<Recipe> findByTypeOrInstOrServings(@Param("type") String type, @Param("instructions") String instructions, @Param("numberOfServings") int numberOfServings);

        @Query(nativeQuery=true, value = "SELECT * FROM RECIPES r WHERE r.type = :type and r.instructions LIKE CONCAT('%',:instructions,'%') and r.number_of_servings >= :numberOfServings and r.id in (SELECT distinct recipe_id from RECIPES_INGREDIENT ri where ri.recipe_id NOT IN  (SELECT recipe_id from RECIPES_INGREDIENT ri where ri.ingredient_id IN ( SELECT id from INGREDIENTS WHERE ingredient IN (:excludeIngredients))))")
        List<Recipe> findByTypeOrInstOrServingsWithExclude(@Param("type") String type, @Param("instructions") String instructions, @Param("numberOfServings") int numberOfServings, @Param("excludeIngredients") List<String> excludeIngredients);

        @Query(nativeQuery=true, value = "SELECT * FROM RECIPES r WHERE r.type = :type and r.instructions LIKE CONCAT('%',:instructions,'%') and r.number_of_servings >= :numberOfServings and r.id in (SELECT distinct recipe_id from RECIPES_INGREDIENT ri where ri.ingredient_id IN (SELECT id from INGREDIENTS WHERE ingredient IN (:includeIngredients)) and ri.recipe_id NOT IN  (SELECT recipe_id from RECIPES_INGREDIENT ri where ri.ingredient_id IN ( SELECT id from INGREDIENTS WHERE ingredient IN (:excludeIngredients))))")
        List<Recipe> findByTypeOrInstOrServingsWithAll(@Param("type") String type, @Param("instructions") String instructions, @Param("numberOfServings") int numberOfServings, @Param("includeIngredients") List<String> includeIngredients, @Param("excludeIngredients") List<String> excludeIngredients);
}
