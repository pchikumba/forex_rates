package com.wiremit.forex_rates.controller;

import com.wiremit.forex_rates.auth.dto.Response;
import com.wiremit.forex_rates.dto.CurrencyDTO;
import com.wiremit.forex_rates.service.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }
    @PostMapping("/add")
    public Response addCurrency(@RequestBody CurrencyDTO request) {
        return currencyService.createCurrency(request);
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllCurrencies() {
        return new ResponseEntity<>(currencyService.getCurrencies(), HttpStatus.OK);
    }
}
