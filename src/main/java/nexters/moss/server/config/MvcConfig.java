package nexters.moss.server.config;

import nexters.moss.server.web.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/user/report");
//                .addPathPatterns("/habit")
//                .addPathPatterns("/habit/history")
//                .addPathPatterns("/habit/record")
//                .addPathPatterns("/cake")
//                .addPathPatterns("/diary/*")
    }
}