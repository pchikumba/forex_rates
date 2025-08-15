package com.wiremit.forex_rates.auth.dto;

import lombok.Data;

@Data
public class LoginCredentials {
    private String email;
    private String password;
}
