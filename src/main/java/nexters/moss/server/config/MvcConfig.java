package nexters.moss.server.config;

import nexters.moss.server.application.dto.Image;
import nexters.moss.server.config.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;
    @Autowired
    private Image image;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/api/user")
                .addPathPatterns("/api/user/report")
                .addPathPatterns("/api/habit")
                .addPathPatterns("/api/habit/history")
                .addPathPatterns("/api/habit/order")
                .addPathPatterns("/api/record")
                .addPathPatterns("/api/cake")
                .addPathPatterns("/api/diary/*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/images/**")
                .addResourceLocations("file://" + image.getLocation());

    }
}