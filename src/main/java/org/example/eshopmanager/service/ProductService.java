package org.example.eshopmanager.service;

import org.example.eshopmanager.entity.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {

    List<Product> getAll();

    Product getProduct(Long id);

    Map<String ,Integer> getProductStock(Long id);

    Product buy(Long id, int amount);

    Product refill(Long id, int amount);
}
