package com.wiremit.forex_rates.auth.service;


import com.wiremit.forex_rates.auth.dto.LoginCredentials;
import com.wiremit.forex_rates.auth.dto.RegisterDTO;
import com.wiremit.forex_rates.auth.dto.RegisterResponse;
import com.wiremit.forex_rates.auth.dto.Response;

public interface AuthService {
    RegisterResponse register(RegisterDTO registerDTO);
    Response registerUser(RegisterDTO registerDTO);
    Response login(LoginCredentials loginCredentials);
}
