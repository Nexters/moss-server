package nexters.moss.server.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        List global = new ArrayList();
        global.add(new ParameterBuilder()
                .name("habikeryToken")
                .description("Authorization")
                .parameterType("header")
                .required(false)
                .modelRef(new ModelRef("string"))
                .build());

        global.add(new ParameterBuilder()
                .name("accessToken")
                .description("Authorization")
                .parameterType("header")
                .required(false)
                .modelRef(new ModelRef("string"))
                .build());

        // TODO : 'ignoredParameterTypes' will ignore those return types as well
        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(global)
                .select()
                .apis(RequestHandlerSelectors.any())
                // error.* 제외. 스프링에서 기본 설정된것.
                .paths(PathSelectors.ant("/api/**"))
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();
    }
}