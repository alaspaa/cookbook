package our.family.cookbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("${request.mapping}")
public class Controller {
    @Autowired
    List<Recipe> cookbook;

    @GetMapping
    public List<Recipe> getRecipes(@RequestParam(required = false) String ingredient) {
        if (ingredient == null || ingredient.isEmpty()) {
            return cookbook;
        } else {
            return cookbook.stream().filter(item -> hasIngredient(item.getIngredients(), ingredient)).collect(Collectors.toList());
        }
    }

    private boolean hasIngredient(List<Ingredient> ingredients, String requestedIngredient) {
        return ingredients.stream().anyMatch(ingredient -> ingredient.getName().toLowerCase().equals(requestedIngredient.toLowerCase()));
    }

    @GetMapping("/{id}")
    public Recipe getRecipesById(@PathVariable String id) {
        Optional<Recipe> retval = cookbook.stream().filter(recipe -> recipe.getName().equals(id)).findFirst();

        if(retval.isPresent()) {
            return retval.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found");
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Recipe addRecipe(@RequestBody Recipe recipe) {
        if(cookbook.stream().anyMatch(item -> recipe.getName().toLowerCase().equals(item.getName()))) {
            return updateRecipe(recipe);
        } else {
            cookbook.add(recipe);
            return recipe;
        }

    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Recipe updateRecipe(@RequestBody Recipe recipe) {
        String id = recipe.getName();
        cookbook = cookbook.stream().map(item -> {
            if(item.getName().equals(id)) {
                item = recipe;
            }
            return item;
        }).collect(Collectors.toList());

         return recipe;
    }
}
