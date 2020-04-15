package nexters.moss.server.config;

import lombok.Data;
import nexters.moss.server.application.dto.Image;
import nexters.moss.server.domain.value.CategoryType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "resources")
public class ResourcesProperties {
    private Image image;
    private List<CategoryType> categories;
}
