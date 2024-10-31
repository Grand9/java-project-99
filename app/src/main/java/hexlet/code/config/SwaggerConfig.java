package hexlet.code.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Swagger API documentation.
 * This class is designed for extension. If you plan to subclass it, ensure
 * that you override the 'api' method properly.
 */
@Configuration
public class SwaggerConfig {
    /**
     * Creates a GroupedOpenApi bean for the public API.
     * <p>
     * This method defines the paths and groups for the API documentation.
     * </p>
     *
     * @return the GroupedOpenApi instance
     */
    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/**")
                .build();
    }
}
