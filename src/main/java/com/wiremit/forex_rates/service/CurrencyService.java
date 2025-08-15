package com.wiremit.forex_rates.service;

import com.wiremit.forex_rates.auth.dto.Response;
import com.wiremit.forex_rates.dto.CurrencyDTO;
import com.wiremit.forex_rates.model.Currency;

import java.util.List;

public interface CurrencyService {
    CurrencyDTO getCurrencyByCode(String code);
    Response createCurrency(CurrencyDTO currency);
    List<CurrencyDTO> getCurrencies();
}
