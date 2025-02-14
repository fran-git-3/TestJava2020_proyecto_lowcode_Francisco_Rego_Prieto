package com.example.priceservice.service;

import com.example.priceservice.model.Price;
import com.example.priceservice.repository.PriceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PriceService {

    private final PriceRepository priceRepository;

    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public Optional<Price> getApplicablePrice(Integer productId, Integer brandId, LocalDateTime date) {
        try {
            System.out.println("Obteniendo precio para ProductId: " + productId + ", BrandId: " + brandId + ", Fecha: " + date);
            List<Price> prices = priceRepository.findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfterOrderByPriorityDesc(
                    productId, brandId, date, date
            );

            System.out.println("Se encontraron " + prices.size() + " precios aplicables.");

            if (prices.isEmpty()) {
                System.out.println("No se encontraron precios aplicables para los parámetros proporcionados.");
            }

            return prices.stream().findFirst();

        } catch (Exception e) {

            System.err.println("Error al obtener el precio aplicable: " + e.getMessage());

            e.printStackTrace();

            System.err.println("Error de consulta o acceso a datos. Devolviendo Optional vacío.");

            return Optional.empty();

        }
    }


}
