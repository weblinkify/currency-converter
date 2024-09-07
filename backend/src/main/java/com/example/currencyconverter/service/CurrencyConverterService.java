package com.example.currencyconverter.service;

import com.example.currencyconverter.dto.ExchangeRate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CurrencyConverterService {

    @Value("${swop.api.url}")
    private String swopApiUrl;

    private String swopApiKey = System.getenv("SWOP_API_KEY");

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final Logger logger = Logger.getLogger(CurrencyConverterService.class.getName());

    public CurrencyConverterService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Cacheable("exchangeRates")
    public double convert(String sourceCurrency, String targetCurrency, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be non-negative.");
        }
        
        try {
            String url = String.format("%s?from=%s&to=%s", 
                swopApiUrl, 
                URLEncoder.encode(sourceCurrency, StandardCharsets.UTF_8.toString()), 
                URLEncoder.encode(targetCurrency, StandardCharsets.UTF_8.toString()));

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "ApiKey " + swopApiKey);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            logger.info("API Response: " + response.getBody());

            List<ExchangeRate> exchangeRates = objectMapper.readValue(response.getBody(), new TypeReference<List<ExchangeRate>>() {});
            
            if (exchangeRates == null || exchangeRates.isEmpty()) {
                logger.warning("No exchange rates found in API response.");
                throw new RuntimeException("No exchange rate data found.");
            }

            Optional<ExchangeRate> exchangeRateOpt = exchangeRates.stream()
                .filter(rate -> rate.getBase_currency().equals(sourceCurrency) && rate.getQuote_currency().equals(targetCurrency))
                .findFirst();

            if (exchangeRateOpt.isEmpty()) {
                logger.warning("Exchange rate not found for " + sourceCurrency + " to " + targetCurrency);
                throw new RuntimeException("Exchange rate not found for " + sourceCurrency + " to " + targetCurrency);
            }

            ExchangeRate exchangeRate = exchangeRateOpt.get();
            logger.info("Exchange Rate: " + exchangeRate.getQuote());

            return exchangeRate.getQuote() * amount;
        } catch (HttpClientErrorException e) {
            logger.log(Level.SEVERE, "Error fetching currency converter: " + e.getMessage(), e);
            throw new RuntimeException("Error fetching currency converter: " + e.getMessage(), e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error parsing exchange rate data: " + e.getMessage(), e);
            throw new RuntimeException("Error parsing exchange rate data: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        }
    }
}
