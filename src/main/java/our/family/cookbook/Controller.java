package our.family.cookbook;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("${request.mapping}")
public class Controller {
    @Autowired
    List<Recipe> cookbook;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping
    public List<Recipe> getRecipes(@RequestParam(required = false) String ingredient) {
        if (ingredient == null || ingredient.isEmpty()) {
            return cookbook;
        } else {
            List<Recipe> recipes = new ArrayList<>();
            for (Recipe r : cookbook) {
                if (hasIngredient(r, ingredient)) {
                    recipes.add(r);
                }
            }
            return recipes;
        }
    }

    private boolean hasIngredient(Recipe recipe, String ingredient) {
        for (Ingredient i : recipe.getIngredients()) {
            if (ingredient.toLowerCase().equals(i.getName().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @GetMapping("/{id}")
    public Recipe getRecipesById(@PathVariable String id) {
        for (Recipe r : cookbook) {
            if (id.equals(r.getName())) {
                return r;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found");
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public String addRecipe(@RequestBody Recipe recipe) {
        cookbook.add(recipe);
        return "Recipe added to cookbook";
    }
}
