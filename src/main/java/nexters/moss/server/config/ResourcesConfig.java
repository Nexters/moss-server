package nexters.moss.server.config;

import nexters.moss.server.application.dto.Image;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
