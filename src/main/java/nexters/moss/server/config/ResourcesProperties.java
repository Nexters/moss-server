package nexters.moss.server.config;

import lombok.Data;
import nexters.moss.server.application.dto.Image;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "resources")
public class ResourcesProperties {
    private Image image;
}
