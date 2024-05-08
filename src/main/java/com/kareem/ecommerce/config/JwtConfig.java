package com.kareem.ecommerce.config;

import com.kareem.ecommerce.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(secret, expiration);
    }
}
