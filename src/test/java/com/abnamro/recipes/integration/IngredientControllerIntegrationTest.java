package com.abnamro.recipes.integration;

import com.abnamro.recipes.RecipeTestData;
import com.abnamro.recipes.mapper.IngredientMapper;
import com.abnamro.recipes.model.Ingredient;
import com.abnamro.recipes.model.request.IngredientRequest;
import com.abnamro.recipes.repository.IngredientRepository;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class IngredientControllerIntegrationTest {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setUp() {
        ingredientRepository.deleteAll();
    }

    @Test
    public void test_saveIngredient_success() throws Exception{

        IngredientRequest request = RecipeTestData.createIngredientRequest();

        MvcResult result = mockMvc.perform(post("/api/v1/ingredient")
                .content(convertToJson(request))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        Integer id = readFromMvcResult(result, "$.id");
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(id);
        assertTrue(optionalIngredient.isPresent());
        assertEquals(optionalIngredient.get().getName(), request.getName());

    }

    @Test
    public void test_saveIngredient_error() throws Exception{

        ingredientRepository.save(RecipeTestData.formIngredient());
        IngredientRequest request = RecipeTestData.createIngredientRequest();

        mockMvc.perform(post("/api/v1/ingredient")
                        .content(convertToJson(request))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void test_getIngredient_notFound() throws Exception {
            mockMvc.perform(get("/api/v1/ingredient/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void test_getIngredient_badRequest() throws Exception {
        mockMvc.perform(get("/api/v1/ingredient/1222ww"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_getIngredient_success() throws Exception {
        Ingredient ingredient = RecipeTestData.formIngredient();
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        mockMvc.perform(get("/api/v1/ingredient/" + savedIngredient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedIngredient.getId()))
                .andExpect(jsonPath("$.name").value(savedIngredient.getName()));
    }

    @Test
    public void test_getAllIngredients() throws Exception {

        List<Ingredient> storeIngredients = RecipeTestData.getIngredients();

        ingredientRepository.saveAll(storeIngredients);

        MvcResult result = mockMvc.perform(get("/api/v1/ingredient"))
                .andExpect(status().isOk())
                .andReturn();

        List<Ingredient> ingredients = getDataFromMvcResult(result, Ingredient.class);

        assertEquals(storeIngredients.size(), ingredients.size());
        assertEquals(storeIngredients.get(0).getName(), ingredients.get(0).getName());
        assertEquals(storeIngredients.get(1).getName(), ingredients.get(1).getName());
    }


    @Test
    public void test_deleteIngredient_success() throws Exception {
        Ingredient ingredient = RecipeTestData.formIngredient();
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        mockMvc.perform(delete("/api/v1/ingredient/{id}", savedIngredient.getId()))
                .andExpect(status().isOk());

        Optional<Ingredient> deletedIngredient = ingredientRepository.findById(savedIngredient.getId());

        assertTrue(deletedIngredient.isEmpty());
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
