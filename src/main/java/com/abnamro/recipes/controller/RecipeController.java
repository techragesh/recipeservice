package com.abnamro.recipes.controller;

import com.abnamro.recipes.model.Recipe;
import com.abnamro.recipes.model.request.RecipeRequest;
import com.abnamro.recipes.model.request.SearchRequest;
import com.abnamro.recipes.model.response.GenericResponse;
import com.abnamro.recipes.service.RecipeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/recipe")
@Api(value = "RecipeController", tags = "Recipe Controller")
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @ApiOperation(value = "Create a recipe")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Recipe created"),
            @ApiResponse(code = 400, message = "Bad input")
    })
    @PostMapping
    public ResponseEntity<Recipe> saveRecipe (
            @ApiParam(value = "Input of the recipe", required = true) @Valid @RequestBody RecipeRequest request) {
        log.info("Creating new recipe");
        Recipe response = recipeService.saveRecipe(request, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @ApiOperation(value = "Update a recipe")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Recipe updated"),
            @ApiResponse(code = 400, message = "Bad input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe (
            @ApiParam(value = "Recipe ID", required = true) @NotNull(message = "Please provide valid ID") @PathVariable(name = "id") Integer id,
            @ApiParam(value = "Input of the recipe", required = true) @Valid @RequestBody RecipeRequest request) {
        log.info("Updating new recipe");
        Recipe response = recipeService.saveRecipe(request, id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiOperation(value = "Get the recipe by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Recipe Overview"),
            @ApiResponse(code = 400, message = "Bad input")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(
            @ApiParam(value = "Recipe ID", required = true) @NotNull(message = "Please provide valid ID") @PathVariable(name = "id") Integer id) {
        log.info("Get Recipe Details");
        Recipe response = recipeService.getRecipeById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiOperation(value = "Get all recipes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Recipes Overview"),
            @ApiResponse(code = 400, message = "Bad input")
    })
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        log.info("Get Recipe Details");
        List<Recipe> response = recipeService.getAllRecipes();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiOperation(value = "Delete the recipe")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 400, message = "Invalid input"),
            @ApiResponse(code = 404, message = "Recipe not found by the given ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteRecipe(
            @ApiParam(value = "Recipe ID", required = true) @NotNull(message = "Please provide valid ID") @PathVariable(name = "id") Integer id) {
        log.info("Deleting the recipe by this id {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(recipeService.deleteRecipe(id));
    }

    @PostMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipe(@ApiParam(value = "Search for the recipe", required = true) @Valid @RequestBody SearchRequest request) {
        log.info("Search Recipe Details");
        List<Recipe> response = recipeService.searchRecipe(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
