package nexters.moss.server.config;

import nexters.moss.server.application.dto.Image;
import nexters.moss.server.domain.Category;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties({ResourcesProperties.class})
public class ResourcesConfig {

    @Bean
    public ResourcesProperties resourcesProperties() {
        return new ResourcesProperties();
    }

    @Bean
    public Image image() {
        return resourcesProperties().getImage();
    }

    @Bean
    @Qualifier("categories")
    public List<Category> categories() {
        return resourcesProperties().getCategories();
    }
}
