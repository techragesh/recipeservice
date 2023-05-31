package com.abnamro.recipes.controller;

import com.abnamro.recipes.model.Ingredient;
import com.abnamro.recipes.model.request.IngredientRequest;
import com.abnamro.recipes.model.response.GenericResponse;
import com.abnamro.recipes.service.IngredientService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/ingredient")
@Api(value = "IngredientController", tags = "Ingredient Controller")
@Slf4j
public class IngredientController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @ApiOperation(value = "Get an ingredient by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ingredient Overview by ID"),
            @ApiResponse(code = 400, message = "Bad input")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getIngredientById(
            @ApiParam(value = "ingredient ID", required = true) @NotNull(message = "Please provide valid ID") @PathVariable(name = "id") Integer id) {
        log.info("Create new ingredient");
        Ingredient response = ingredientService.getIngredientById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiOperation(value = "Get All Ingredients")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ingredient Overview"),
            @ApiResponse(code = 400, message = "Bad input")
    })
    @GetMapping
    public ResponseEntity<List<Ingredient>> getAllIngredient() {
        log.info("Create new ingredient");
        List<Ingredient> response = ingredientService.getAllIngredients();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiOperation(value = "Create an ingredient")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Ingredient created"),
            @ApiResponse(code = 400, message = "Bad input")
    })
    @PostMapping
    public ResponseEntity<Ingredient> saveIngredient(
            @ApiParam(value = "Input of the Ingredient", required = true) @Valid @RequestBody IngredientRequest request) {
        log.info("Create new ingredient");
        Ingredient response = ingredientService.saveIngredient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @ApiOperation(value = "Delete the ingredient")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 400, message = "Invalid input"),
            @ApiResponse(code = 404, message = "Ingredient not found by the given ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteIngredient(@ApiParam(value = "ingredient ID", required = true) @NotNull(message = "Please provide valid ID") @PathVariable(name = "id") Integer id) {
        log.info("Deleting the ingredient by this id {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(ingredientService.delete(id));
    }
}
