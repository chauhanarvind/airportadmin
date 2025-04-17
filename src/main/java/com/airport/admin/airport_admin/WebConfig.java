package com.airport.admin.airport_admin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/**") //allow all endpoints
                        .allowedOrigins("http://localhost:3000") //base url of React
                        .allowedMethods("GET", "POST", "PUT", "DELETE") //allow all methods
                        .allowedHeaders("*");
            }
        };
    }
}
