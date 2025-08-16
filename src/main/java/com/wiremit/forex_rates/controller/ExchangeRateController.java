package com.wiremit.forex_rates.controller;

import com.wiremit.forex_rates.model.ExchangeRate;
import com.wiremit.forex_rates.service.ExchangeRateService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/rates")
    public ResponseEntity<?> getCurrentRates() {
        return ResponseEntity.ok(exchangeRateService.getBankStyleRates());
    }

    @GetMapping("/rates/{currency}")
    public ResponseEntity<?> getRateByCurrency(@PathVariable String currency) {
        return ResponseEntity.ok(exchangeRateService.getRateByBaseCurrency(currency));
    }

    @GetMapping("/historical/rates")
    public ResponseEntity<List<ExchangeRate>> getHistoricalRates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(exchangeRateService.getHistoricalRates(from, to));
    }
}

