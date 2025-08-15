package com.wiremit.forex_rates.service;

import com.wiremit.forex_rates.dto.CurrencyDTO;
import com.wiremit.forex_rates.dto.ERApiResponse;
import com.wiremit.forex_rates.dto.FrankfurterResponse;
import com.wiremit.forex_rates.enums.ForexProvider;
import com.wiremit.forex_rates.exceptions.CustomException;
import com.wiremit.forex_rates.model.ExchangeRate;
import com.wiremit.forex_rates.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationClient {
    @Value("${frank_furter.url}")
    private String frankUrl;
    @Value("${open_er.url}")
    private String openErUrl;
    @Value("${forex.markup:0.10}")
    private BigDecimal markup;
    private final WebClient webClient = WebClient.builder().build();
    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyService currencyService;

    public Mono<FrankfurterResponse> getAllRatesFromFrankfurter(String base) {
        return webClient.get()
                .uri(frankUrl + base)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(FrankfurterResponse.class);
    }


    public Mono<ERApiResponse> getRatesFromERApi(String base) {
        return webClient.get()
                .uri(openErUrl + base)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ERApiResponse.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public void fetchAndSaveAllRates(String baseCurrency) throws InterruptedException {

        if (baseCurrency == null || baseCurrency.isBlank()) {
            log.error("Base Currency are empty");
            throw new CustomException("baseCurrency does not exist");
        }

        Set<String> allowedCurrencies = currencyService.getCurrencies()
                .stream()
                .map(CurrencyDTO::getCode)
                .collect(Collectors.toSet());

        if (allowedCurrencies.isEmpty()) {
            log.error("Currencies are empty");
            throw new CustomException("No currencies found to pull current rates");
        }

        Mono<FrankfurterResponse> frankfurterMono = getAllRatesFromFrankfurter(baseCurrency);
        Mono<ERApiResponse> erApiMono = getRatesFromERApi(baseCurrency);

        Mono.zip(frankfurterMono, erApiMono)
                .map(tuple -> {
                    FrankfurterResponse frankfurter = tuple.getT1();
                    ERApiResponse erapi = tuple.getT2();

                    saveRates(frankfurter.getBase(), frankfurter.getRates(), allowedCurrencies, ForexProvider.FRANK_FURTER.name());
                    saveRates(erapi.getBaseCode(), erapi.getRates(), allowedCurrencies, ForexProvider.OPEN_ER.name());

                    return true;
                }).block();
    }

    private void saveRates(String baseCurrency, Map<String, Double> rates,
                           Set<String> allowedCurrencies, String provider) {
        rates.entrySet().stream()
                .filter(entry -> allowedCurrencies.contains(entry.getKey()))
                .forEach(entry -> {
                    BigDecimal rateValue = BigDecimal.valueOf(entry.getValue());
                    ExchangeRate record = ExchangeRate.builder()
                            .baseCurrency(baseCurrency)
                            .targetCurrency(entry.getKey())
                            .rate(rateValue)
                            .markedUpRate(rateValue.add(markup))
                            .providerName(provider)
                            .currentRate(true)
                            .build();
                    exchangeRateRepository.save(record);
                });
    }

    public void updateHistoricalRates() {
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findAllByCurrentRate(true);
        if (exchangeRates.isEmpty()) {
            return;
        }
        exchangeRates.forEach(exchangeRate -> {
            exchangeRate.setCurrentRate(false);
            exchangeRateRepository.save(exchangeRate);
        });
    }

}
