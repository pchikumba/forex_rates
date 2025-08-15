package com.wiremit.forex_rates.service;

import com.wiremit.forex_rates.dto.CurrentRatesDTO;
import com.wiremit.forex_rates.model.ExchangeRate;
import com.wiremit.forex_rates.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    @Value("${forex.markup}")
    private  BigDecimal MARKUP;

    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public List<ExchangeRate> getCurrentRates() {
        return exchangeRateRepository.findLatestRates();
    }

    @Override
    public ExchangeRate getRateByBaseCurrency(String baseCurrency) {
        return null;
    }

    public ExchangeRate getRateByCurrency(String baseCurrency, String targetCurrency) {
        return exchangeRateRepository.findLatestRate(baseCurrency,targetCurrency)
                .orElseThrow(() -> new RuntimeException("Rate not found for currency: " + baseCurrency));
    }

    public List<ExchangeRate> getHistoricalRates(LocalDateTime from, LocalDateTime to) {
        return exchangeRateRepository.findHistoricalRates(from, to);
    }

    @Override
    public List<CurrentRatesDTO> getBankStyleRates() {
        List<ExchangeRate> currentRates = exchangeRateRepository.findAllByCurrentRate(true);

        // Group by baseCurrency + targetCurrency
        Map<String, List<ExchangeRate>> groupedRates = currentRates.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getBaseCurrency() + "_" + r.getTargetCurrency()
                ));

        List<CurrentRatesDTO> results = new ArrayList<>();

        for (Map.Entry<String, List<ExchangeRate>> entry : groupedRates.entrySet()) {
            List<ExchangeRate> rates = entry.getValue();

            // Calculate average rate
            BigDecimal avgRate = rates.stream()
                    .map(ExchangeRate::getRate)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(rates.size()), 6, RoundingMode.HALF_UP);

            // Apply markup
            BigDecimal markedUpRate = avgRate.add(avgRate.multiply(MARKUP))
                    .setScale(6, RoundingMode.HALF_UP);

            // Extract currencies from key
            String[] parts = entry.getKey().split("_");
            results.add(new CurrentRatesDTO(parts[0], parts[1], markedUpRate));
        }

        return results;
    }
}
