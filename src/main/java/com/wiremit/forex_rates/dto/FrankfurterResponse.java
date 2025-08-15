package com.wiremit.forex_rates.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class FrankfurterResponse {

    private double amount;
    private String base;
    private String date;

    private Map<String, Double> rates;

    @JsonProperty("amount")
    public void setAmount(double amount) {
        this.amount = amount;
    }
}