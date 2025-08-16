package com.wiremit.forex_rates.service;

import com.wiremit.forex_rates.dto.CurrencyDTO;
import com.wiremit.forex_rates.exceptions.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CurrencyRateCron {
    private final AggregationClient aggregationClient;
    private final CurrencyService currencyService;

    public CurrencyRateCron(AggregationClient aggregationClient, CurrencyService currencyService) {
        this.aggregationClient = aggregationClient;
        this.currencyService = currencyService;
    }

    //@Scheduled(fixedRate = 3600000)
    public void pullCurrencyRate() throws InterruptedException {
        var currencies = currencyService.getCurrencies();
        if (currencies.isEmpty()) {
            log.info("No currencies found");
            return;
        }
        aggregationClient.updateHistoricalRates();
        Thread.sleep(5000);
        for (CurrencyDTO currency: currencies) {
            try {
                aggregationClient.fetchAndSaveAllRates(currency.getCode());
            } catch (InterruptedException e) {
                throw new CustomException("Error in fetching forex rates from providers "+e.getMessage());
            }
        }
    }
}
