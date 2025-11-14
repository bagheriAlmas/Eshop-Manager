package org.example.eshopmanager.controller;


import lombok.RequiredArgsConstructor;
import org.example.eshopmanager.entity.Product;
import org.example.eshopmanager.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<Integer> getStock(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductStock(id));
    }

    @PostMapping("/{id}/refill")
    public ResponseEntity<Product> refillStock(@PathVariable Long id, @RequestParam int amount) {
        return ResponseEntity.ok(productService.refill(id, amount));
    }

    @PostMapping("/{id}/buy")
    public ResponseEntity<Product> buyProduct(@PathVariable Long id, @RequestParam int amount) {
        return ResponseEntity.ok(productService.buy(id, amount));
    }
}
