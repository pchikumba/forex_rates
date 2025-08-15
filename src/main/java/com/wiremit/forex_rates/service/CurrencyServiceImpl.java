package com.wiremit.forex_rates.service;

import com.wiremit.forex_rates.auth.dto.Response;
import com.wiremit.forex_rates.dto.CurrencyDTO;
import com.wiremit.forex_rates.model.Currency;
import com.wiremit.forex_rates.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public CurrencyDTO getCurrencyByCode(String code) {
        return mapToDTO(currencyRepository.getCurrencyByCode(code));
    }

    @Override
    public Response createCurrency(CurrencyDTO request) {

        String code = request.getCode().toUpperCase();
        var existingCurrency = currencyRepository.getCurrencyByCode(code);
        if (existingCurrency != null) {
            return createCurrencyResponse("Currency with code " + code + " already exists", mapToDTO(existingCurrency));
        }
        Currency newCurrency = Currency.builder()
                .code(code)
                .name(request.getName())
                .build();
        currencyRepository.save(newCurrency);

        return createCurrencyResponse("Currency with code " + code + " created", mapToDTO(newCurrency));
    }

    @Override
    public List<CurrencyDTO> getCurrencies() {
        List<Currency> currencies = currencyRepository.findAll();
        return mapToDTOList(currencies);
    }

    private CurrencyDTO mapToDTO(Currency currency) {
        return CurrencyDTO.builder()
                .code(currency.getCode())
                .name(currency.getName())
                .build();
    }

    private List<CurrencyDTO> mapToDTOList(List<Currency> currencies) {
        return currencies.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private Response createCurrencyResponse(String message, CurrencyDTO data) {
        var response = new Response();
        response.setMessage(message);
        response.setData(data);
        return response;
    }
}
