package com.kareem.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable CSRF for specific use cases (e.g., REST APIs)
        http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                            "/",
                            "/public/**",
                            "/api/**"
                            ).permitAll(); // Public access
                    auth.requestMatchers("/admin/**").hasRole("ADMIN");  // Admin-only access
                    auth.anyRequest().authenticated(); // All other requests require authentication
                })
                .formLogin(form -> {
                    form.loginPage("/login").permitAll(); // Custom login page
                    form.defaultSuccessUrl("/", true); // Redirect after successful login
                })
                .logout(logout -> {
                    logout.logoutUrl("/logout"); // URL to log out
                    logout.logoutSuccessUrl("/").permitAll(); // Allow everyone to log out
                })
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Secure password hashing
    }
}
