package com.wiremit.forex_rates.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
   private int statusCode;
   private String message;
    private Object data;
}
