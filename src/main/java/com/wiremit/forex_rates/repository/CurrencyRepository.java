package com.wiremit.forex_rates.repository;

import com.wiremit.forex_rates.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency getCurrencyByCode(String code);
}
