package com.mycompany.platforme_telemedcine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
        // CORS config for WebSocket endpoints
        CorsConfiguration wsConfig = new CorsConfiguration();
        wsConfig.addAllowedOrigin("http://localhost:5173");
        wsConfig.addAllowedOrigin("http://localhost:5174");
        wsConfig.addAllowedOrigin("http://localhost:3000");
        wsConfig.addAllowedHeader("*");
        wsConfig.addAllowedMethod("*");
        wsConfig.setAllowCredentials(true);
        source.registerCorsConfiguration("/ws/**", wsConfig);
        
        // CORS config for all other endpoints
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("http://localhost:5174");
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}

