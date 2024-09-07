package com.example.currencyconverter.controller;

import com.example.currencyconverter.service.CurrencyConverterService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Validated
public class CurrencyController {

    private final CurrencyConverterService currencyConverterService;

    public CurrencyController(CurrencyConverterService currencyConverterService) {
        this.currencyConverterService = currencyConverterService;
    }

    @GetMapping("/convert")
    public ResponseEntity<String> convertCurrency(
            @RequestParam @NotBlank String from,
            @RequestParam @NotBlank String to,
            @RequestParam @Min(value = 0, message = "Amount must be positive.") double amount) {

        try {
            double result = currencyConverterService.convert(from, to, amount);
            String formattedResult = String.format("%.6f", result);
            return ResponseEntity.ok(formattedResult);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Conversion failed due to an unexpected error.");
        }
    }
}
