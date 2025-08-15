package com.wiremit.forex_rates.auth.controller;


import com.wiremit.forex_rates.auth.config.JWTUtil;
import com.wiremit.forex_rates.auth.dto.*;
import com.wiremit.forex_rates.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JWTUtil jwtUtil;


    @PostMapping("/register")
    public Response register(@Valid @RequestBody RegisterDTO registerDTO) {
        return authService.registerUser(registerDTO);
    }

    @PostMapping("/login")
    public Response login(@Valid @RequestBody LoginCredentials loginCredentials) {
        return authService.login(loginCredentials);
    }

    @PostMapping("/validate")
    public ResponseEntity<Object> validateToken(@Valid @RequestBody ValidateTokenRequest request) {
        return ResponseHandler.generateResponse("Token is valid", HttpStatus.OK, jwtUtil.validateToken(request.getToken()));
    }
}
