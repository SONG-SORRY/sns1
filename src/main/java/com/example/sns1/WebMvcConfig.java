package com.example.sns1;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String projectPath = System.getProperty("user.dir");
        
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:///" + projectPath + "/src/main/resources/static/files/");
    }
}
