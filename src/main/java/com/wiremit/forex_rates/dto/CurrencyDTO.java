package com.wiremit.forex_rates.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CurrencyDTO {
    private String code;
    private String name;
}
