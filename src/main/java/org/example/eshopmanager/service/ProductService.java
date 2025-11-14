package org.example.eshopmanager.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.eshopmanager.entity.Product;
import org.example.eshopmanager.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("product not found"));
    }

    @Transactional
    public Product buy(Long id, int amount) {
        final var product = getProduct(id);

        if (product.getStock() < amount)
            throw new RuntimeException("product not enough");

        product.setStock(product.getStock() - amount);

        return productRepository.save(product);
    }

    @Transactional
    public Product refill(Long id, int amount) {
        final var product = getProduct(id);
        product.setStock(product.getStock() + amount);
        return productRepository.save(product);
    }
}
