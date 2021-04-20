package our.family.cookbook;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CookbookApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate template;

    @Autowired
    private List<Recipe> cookBook;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test_AllRecipes() throws JsonProcessingException {
        List<Recipe> recipes = getStringAsRecipeList(template.getForObject(getBaseUrl(), String.class));
        Assertions.assertEquals(3,recipes.size());
    }

    @Test
    public void test_RecipeById() {
        List<String> ids = Arrays.asList(new String[] { "Nutella Powder", "Bittman Chinese Chicken With Bok Choy", "Brogrammer's Dream" });

        for (String id : ids) {
            String url = getBaseUrl() + "/" + id;
            Recipe recipe = template.getForObject(url, Recipe.class);
            Assertions.assertNotNull(recipe);
            Assertions.assertEquals(id, recipe.getName());
        }
    }

    @Test
    public void test_RecipeByIngredient() throws JsonProcessingException {
        for (Recipe r : cookBook) {
            for (Ingredient i : r.getIngredients()) {
                String url = getBaseUrl() + "?ingredient=" + i.getName();
                List<Recipe> recipes = getStringAsRecipeList(template.getForObject(url, String.class));

                Assertions.assertTrue(!recipes.isEmpty());
                recipes.stream().anyMatch(item -> item.getName().equals(r.getName()));
            }
        }
    }

    @Test
    public void test_PostRecipe() throws JsonProcessingException {
        final String MUD_CAKE = "Mud cake";
        Recipe r = new Recipe();
        r.setName(MUD_CAKE);
        r.setInstructions("Do not eat this");
        r.setIngredients(Arrays.asList(new Ingredient[] { new Ingredient("Sand", 1, "kilogram"), new Ingredient("Water", 7, "desiliter") }));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(r), headers);
        String response = template.postForObject(getBaseUrl(), entity, String.class);

        Assertions.assertEquals("Recipe added to cookbook", response);

        List<Recipe> recipes = getStringAsRecipeList(template.getForObject(getBaseUrl(), String.class));
        Assertions.assertEquals(4, recipes.size());
        Assertions.assertTrue(recipes.stream().anyMatch(item -> item.getName().equals(MUD_CAKE)));

        //Clean up
        cookBook.remove(cookBook.size() - 1);
    }

    private List<Recipe> getStringAsRecipeList(String value) throws JsonProcessingException {
        return objectMapper.readValue(value, new TypeReference<List<Recipe>>() {
        });
    }

    private String getBaseUrl() {
        return "http://localhost:" + port + "/recipes";
    }

}
