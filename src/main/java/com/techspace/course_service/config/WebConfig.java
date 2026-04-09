package com.techspace.course_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // =========================
    // SERVE VIDEO + SUBTITLE FILES
    // =========================
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/videos/**")
                .addResourceLocations("file:C:/temper/uploads/videos/")
                .setCachePeriod(3600); // 🔥 optional caching (1 hour)
    }

    // =========================
    // SUPPORT .vtt FILE TYPE
    // =========================
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                .favorPathExtension(true)
                .mediaType("vtt", MediaType.valueOf("text/vtt"));
    }

    // =========================
    // CORS FIX (IMPORTANT FOR VIDEO + SUBTITLES)
    // =========================
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/videos/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}