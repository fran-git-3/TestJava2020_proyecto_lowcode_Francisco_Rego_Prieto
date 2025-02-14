package com.example.priceservice;

import com.example.priceservice.controller.PriceController;
import com.example.priceservice.model.Price;
import com.example.priceservice.service.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PriceServiceApplicationTests {

    @InjectMocks
    private PriceController priceController;

    @Mock
    private PriceService priceService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @BeforeEach
    void setUp() {
        priceService = mock(PriceService.class);
        priceController = new PriceController(priceService);
    }

    @Test
    void getPriceTest_Case1() {
        testPrice("2020-06-14 10:00:00", 1, 35455, 1, "2020-06-14 00:00:00", "2020-12-31 23:59:59", 35.50, "EUR");
    }

    @Test
    void getPriceTest_Case2() {
        testPrice("2020-06-14 16:00:00", 1, 35455, 2, "2020-06-14 15:00:00", "2020-06-14 18:30:00", 25.45, "EUR");
    }

    @Test
    void getPriceTest_Case3() {
        testPrice("2020-06-14 21:00:00", 1, 35455, 1, "2020-06-14 00:00:00", "2020-12-31 23:59:59", 35.50, "EUR");
    }

    @Test
    void getPriceTest_Case4() {
        testPrice("2020-06-15 10:00:00", 1, 35455, 3, "2020-06-15 00:00:00", "2020-06-15 11:00:00", 30.50, "EUR");
    }

    @Test
    void getPriceTest_Case5() {
        testPrice("2020-06-16 21:00:00", 1, 35455, 4, "2020-06-16 16:00:00", "2020-12-31 23:59:59", 38.95, "EUR");
    }


    private void testPrice(String dateStr, int brandId, int productId, int priceList, String startDate, String endDate, double price, String currency) {
        LocalDateTime date = LocalDateTime.parse(dateStr, formatter);

        Price mockPrice = new Price();
        mockPrice.setBrandId(brandId);
        mockPrice.setProductId(productId);
        mockPrice.setPriceList(priceList);
        mockPrice.setStartDate(LocalDateTime.parse(startDate, formatter));
        mockPrice.setEndDate(LocalDateTime.parse(endDate, formatter));
        mockPrice.setPrice(BigDecimal.valueOf(price));
        mockPrice.setCurrency(currency);

        when(priceService.getApplicablePrice(brandId, productId, date)).thenReturn(Optional.of(mockPrice));
        ResponseEntity<Price> response = priceController.getPrice(brandId, productId, dateStr);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockPrice, response.getBody());
    }


}
