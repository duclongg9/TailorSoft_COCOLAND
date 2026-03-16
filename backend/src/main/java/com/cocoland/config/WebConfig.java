package com.cocoland.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir:uploads/}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
            if (!java.nio.file.Files.exists(uploadPath)) {
                java.nio.file.Files.createDirectories(uploadPath);
            }
            
            String location = "file:" + uploadPath.toString() + "/";
            // On Linux/Docker, ensure it starts with /
            if (!location.startsWith("file:/")) {
                location = "file:/" + uploadPath.toString() + "/";
            }

            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations(location)
                    .setCachePeriod(0); // Disable cache for dev/demo to see images immediately
        } catch (Exception e) {
            // Log error
            System.err.println("Could not setup resource handler: " + e.getMessage());
        }
    }
}
