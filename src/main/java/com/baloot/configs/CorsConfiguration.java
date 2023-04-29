package com.baloot.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                List<String> methodList = new ArrayList<>(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                methodList.add("OPTIONS");
                registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods(methodList.toArray(new String[0]))
                    .allowedHeaders("*");
            }
        };
    }
}
