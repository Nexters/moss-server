package nexters.moss.server.config;

import lombok.Data;
import nexters.moss.server.application.dto.S3;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "aws")
public class AWSProperties {
    private S3 s3;
}
