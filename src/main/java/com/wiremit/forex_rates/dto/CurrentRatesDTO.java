package com.wiremit.forex_rates.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class CurrentRatesDTO {
    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal rate;

}
