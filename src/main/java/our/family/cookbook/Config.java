package our.family.cookbook;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@PropertySource("application.properties")
public class Config {

    @Value("${recipe.file.name}")
    private String fileName;

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public String getRecipesAsString() {
        try (InputStream is = Controller.class.getResourceAsStream(fileName)) {
            Objects.requireNonNull(is);
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public List<Recipe> cookbook(String getRecipesAsString, ObjectMapper objectMapper) {
        List<Recipe> recipes = null;

        try {
            recipes = objectMapper.readValue(getRecipesAsString, new TypeReference<List<Recipe>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return recipes;
    }
}
