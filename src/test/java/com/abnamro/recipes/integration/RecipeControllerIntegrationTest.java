package com.abnamro.recipes.integration;

import com.abnamro.recipes.RecipeTestData;
import com.abnamro.recipes.model.Recipe;
import com.abnamro.recipes.model.request.RecipeRequest;
import com.abnamro.recipes.model.request.SearchRequest;
import com.abnamro.recipes.repository.IngredientRepository;
import com.abnamro.recipes.repository.RecipeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RecipeControllerIntegrationTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setUp() {
        recipeRepository.deleteAll();
    }

    @Test
    public void test_saveRecipe_success() throws Exception{

        RecipeRequest request = RecipeTestData.createRecipeRequest();

        MvcResult result = mockMvc.perform(post("/api/v1/recipe")
                .content(convertToJson(request))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        Integer id = readFromMvcResult(result, "$.id");
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        assertTrue(optionalRecipe.isPresent());
        assertEquals(optionalRecipe.get().getName(), request.getName());

    }

    @Test
    public void test_getRecipe_notFound() throws Exception {
            mockMvc.perform(get("/api/v1/recipe/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void test_getRecipe_success() throws Exception {
        Recipe recipe = RecipeTestData.formRecipe();
        Recipe savedRecipe = recipeRepository.save(recipe);
        mockMvc.perform(get("/api/v1/recipe/" + savedRecipe.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedRecipe.getId()))
                .andExpect(jsonPath("$.name").value(savedRecipe.getName()))
                .andExpect(jsonPath("$.instructions").value(savedRecipe.getInstructions()))
                .andExpect(jsonPath("$.numberOfServings").value(savedRecipe.getNumberOfServings()));
    }

    @Test
    public void test_getAllRecipes() throws Exception {

        List<Recipe> storeRecipeList = RecipeTestData.getRecipes();

        recipeRepository.saveAll(storeRecipeList);

        MvcResult result = mockMvc.perform(get("/api/v1/recipe"))
                .andExpect(status().isOk())
                .andReturn();

        List<Recipe> recipeList = getDataFromMvcResult(result, Recipe.class);

        assertEquals(storeRecipeList.size(), recipeList.size());
        assertEquals(storeRecipeList.get(0).getName(), recipeList.get(0).getName());
        assertEquals(storeRecipeList.get(1).getName(), recipeList.get(1).getName());
    }


    @Test
    public void test_updateRecipe_success() throws Exception {
        Recipe testRecipe = RecipeTestData.formRecipe();
        Recipe savedRecipe = recipeRepository.save(testRecipe);

        savedRecipe.setName("pizza veg");
        savedRecipe.setInstructions("added extra cheese and olives");

        RecipeRequest request = RecipeTestData.createRecipeRequest();
        request.setName("pizza veg");
        request.setInstructions("added extra cheese and olives");

        MvcResult result = mockMvc.perform(put("/api/v1/recipe/{id}", savedRecipe.getId())
                        .content(convertToJson(request))
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        Integer id = readFromMvcResult(result, "$.id");
        Optional<Recipe> updatedRecipe = recipeRepository.findById(id);

        assertTrue(updatedRecipe.isPresent());
        assertEquals(savedRecipe.getName(), updatedRecipe.get().getName());
        assertEquals(savedRecipe.getNumberOfServings(), updatedRecipe.get().getNumberOfServings());
        assertEquals(savedRecipe.getInstructions(), updatedRecipe.get().getInstructions());
    }

    @Test
    public void test_updateRecipeById_Invalid() throws Exception {

        RecipeRequest request = RecipeTestData.createRecipeRequest();

        mockMvc.perform(put("/api/v1/recipe/{id}", request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_deleteRecipe_success() throws Exception {
        Recipe testRecipe = RecipeTestData.formRecipe();
        Recipe savedRecipe = recipeRepository.save(testRecipe);
        mockMvc.perform(delete("/api/v1/recipe/{id}", savedRecipe.getId()))
                .andExpect(status().isOk());

        Optional<Recipe> deletedRecipe = recipeRepository.findById(savedRecipe.getId());

        assertTrue(deletedRecipe.isEmpty());
    }

    @Test
    public void test_search_recipe() throws Exception {

        SearchRequest searchRequest = RecipeTestData.createSearchRequest();
        List<Recipe> storeRecipeList = RecipeTestData.searchRecipes();

        recipeRepository.saveAll(storeRecipeList);

        MvcResult result = mockMvc.perform(post("/api/v1/recipe/search")
                        .content(convertToJson(searchRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<Recipe> recipeList = getDataFromMvcResult(result, Recipe.class);

        assertTrue(recipeList.isEmpty());
//        assertEquals(storeRecipeList.size(), recipeList.size());
//        assertEquals(storeRecipeList.get(0).getName(), recipeList.get(0).getName());

    }


    public String convertToJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    private <T> T readFromMvcResult(MvcResult result, String jsonPath) throws UnsupportedEncodingException {
        return JsonPath.read(result.getResponse().getContentAsString(), jsonPath);
    }

    protected <T> List<T> getDataFromMvcResult(MvcResult result, Class<T> listElementClass) throws IOException {
        return objectMapper.readValue(result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, listElementClass));
    }

}
