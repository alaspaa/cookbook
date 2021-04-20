package our.family.cookbook;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@Configuration
@Import({ Config.class })
public class TestConfig {
    @Bean
    RestTemplate template() {
        return new RestTemplate();
    }
}
