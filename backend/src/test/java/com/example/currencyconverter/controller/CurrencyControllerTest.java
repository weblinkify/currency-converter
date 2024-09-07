package com.example.currencyconverter;

import com.example.currencyconverter.controller.CurrencyController;
import com.example.currencyconverter.service.CurrencyConverterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

public class CurrencyControllerTest {

    @Mock
    private CurrencyConverterService currencyConverterService;

    @InjectMocks
    private CurrencyController currencyController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(currencyController).build();
    }

    @Test
    public void testConvertCurrency() throws Exception {
        when(currencyConverterService.convert("USD", "GBP", 30.0)).thenReturn(22.5);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/convert")
                .param("from", "USD")
                .param("to", "GBP")
                .param("amount", "30.0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("22.500000"))
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> System.out.println("Test passed. The response content is: " + result.getResponse().getContentAsString()));
    }
}
