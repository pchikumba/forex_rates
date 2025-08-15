package com.wiremit.forex_rates.service;


import com.wiremit.forex_rates.dto.CurrentRatesDTO;
import com.wiremit.forex_rates.model.ExchangeRate;

import java.time.LocalDateTime;
import java.util.List;

public interface ExchangeRateService {
    List<ExchangeRate> getCurrentRates();
    ExchangeRate getRateByBaseCurrency(String baseCurrency);
    ExchangeRate getRateByCurrency(String baseCurrency, String targetCurrency);
    List<ExchangeRate> getHistoricalRates(LocalDateTime from, LocalDateTime to);
    public List<CurrentRatesDTO> getBankStyleRates();

}
