package com.wiremit.forex_rates.auth.config;



import com.wiremit.forex_rates.auth.dto.UserResponse;
import com.wiremit.forex_rates.auth.model.User;

import java.time.format.DateTimeFormatter;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(String.valueOf(user.getId()));
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        response.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt().format(formatter) : null);
        response.setUpdatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt().format(formatter) : null);

        return response;
    }
}