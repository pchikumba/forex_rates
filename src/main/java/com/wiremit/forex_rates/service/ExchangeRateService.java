package com.wiremit.forex_rates.service;


import com.wiremit.forex_rates.dto.CurrentRatesDTO;
import com.wiremit.forex_rates.model.ExchangeRate;

import java.time.LocalDateTime;
import java.util.List;

public interface ExchangeRateService {
    List<CurrentRatesDTO> getRateByBaseCurrency(String baseCurrency);

    List<ExchangeRate> getHistoricalRates(LocalDateTime from, LocalDateTime to);

    List<CurrentRatesDTO> getBankStyleRates();

}
