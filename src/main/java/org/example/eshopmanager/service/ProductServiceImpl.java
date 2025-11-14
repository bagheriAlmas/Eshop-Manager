package org.example.eshopmanager.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.eshopmanager.entity.Product;
import org.example.eshopmanager.exception.NotEnoughStockException;
import org.example.eshopmanager.exception.ProductNotFoundException;
import org.example.eshopmanager.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Map<String, Integer> getProductStock(Long productId) {
        final var product = getProduct(productId);
        return Collections.singletonMap(product.getName(), product.getStock());
    }

    @Transactional
    public Product buy(Long id, int amount) {
        final var product = getProduct(id);
        final var availableStock = product.getStock() - product.getReservedStock();

        if (availableStock < amount)
            throw new NotEnoughStockException(amount, availableStock);

        product.setStock(product.getStock() - amount);

        final var reserved = Math.min(product.getReservedStock(), amount);
        product.setReservedStock(product.getReservedStock() - reserved);

        return productRepository.save(product);
    }


    @Transactional
    public Product refill(Long id, int amount) {
        final var product = getProduct(id);
        product.setStock(product.getStock() + amount);
        return productRepository.save(product);
    }

    @Transactional
    public Product reserve(Long id, int amount ) {
        Product product = getProduct(id);

        final var availableStock = product.getStock() - product.getReservedStock();
        if (availableStock < amount)
            throw new NotEnoughStockException(amount, availableStock);

        product.setReservedStock(product.getReservedStock() + amount);
        product.setReservationExpiry(LocalDateTime.now().plusMinutes(1));

        return productRepository.save(product);
    }

}
