package com.example.priceservice.controller;

import com.example.priceservice.model.Price;
import com.example.priceservice.service.PriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    private final PriceService priceService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss");


    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping
    public ResponseEntity<Price> getPrice(
            @RequestParam Integer productId,
            @RequestParam Integer brandId,
            @RequestParam String date) {

        LocalDateTime queryDate = LocalDateTime.parse(date, formatter);
        Optional<Price> price = priceService.getApplicablePrice(productId, brandId, queryDate);

        return price.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
