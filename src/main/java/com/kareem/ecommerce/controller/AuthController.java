package com.kareem.ecommerce.controller;

import com.kareem.ecommerce.model.dto.LoginRequestDTO;
import com.kareem.ecommerce.model.dto.LoginResponseDTO;
import com.kareem.ecommerce.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());

            return ResponseEntity.ok(loginResponseDTO); // Return the token and user data
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
