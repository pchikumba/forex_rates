package com.wiremit.forex_rates.repository;

import com.wiremit.forex_rates.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate,Long> {

    @Query("SELECT er FROM ExchangeRate er " +
            "WHERE er.timestamp BETWEEN :start AND :end " +
            "ORDER BY er.timestamp ASC")
    List<ExchangeRate> findHistoricalRates(@Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end);

    List<ExchangeRate> findAllByCurrentRate(Boolean currentRate);
    List<ExchangeRate> findAllByBaseCurrencyAndCurrentRate(String baseCurrency, Boolean currentRate);
}
