package nexters.moss.server.config;

import nexters.moss.server.application.dto.S3;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AWSProperties.class})
public class AWSConfig {

    @Bean
    public AWSProperties awsProperties() {
        return new AWSProperties();
    }

    @Bean
    public S3 s3() {
        return awsProperties().getS3();
    }
}
