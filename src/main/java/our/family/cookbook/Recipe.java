package our.family.cookbook;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    private String name;
    private List<Ingredient> ingredients;
    private String instructions;
}
