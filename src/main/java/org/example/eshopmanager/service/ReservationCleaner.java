package org.example.eshopmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.eshopmanager.entity.Product;
import org.example.eshopmanager.repository.ProductRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationCleaner {

    private final ProductRepository productRepository;

    @Scheduled(fixedRate = 60000)
    public void releaseExpiredReservations() {
        List<Product> products = productRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        products.stream()
                .filter(p -> p.getReservedStock() > 0 && p.getReservationExpiry() != null && p.getReservationExpiry().isBefore(now))
                .forEach(p -> {
                    p.setReservedStock(0);
                    p.setReservationExpiry(null);
                    productRepository.save(p);
                });
    }
}
