package org.example.eshopmanager.service;

import org.example.eshopmanager.entity.Product;
import org.example.eshopmanager.exception.NotEnoughStockException;
import org.example.eshopmanager.exception.ProductNotFoundException;
import org.example.eshopmanager.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

class ProductServiceImplTest {

    private ProductRepository productRepository;
    private ProductServiceImpl productServiceImpl;

    @BeforeEach
    void setup() {
        productRepository = Mockito.mock(ProductRepository.class);
        productServiceImpl = new ProductServiceImpl(productRepository);
    }

    @Test
    void getAll_ReturnsAllProducts() {
        final var products = List.of(
                new Product(1L, "P1", 10d, 100, 0),
                new Product(2L, "P2", 20d, 100, 0)
        );

        when(productRepository.findAll()).thenReturn(products);

        final var result = productServiceImpl.getAll();

        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProduct_WhenExists_ReturnsProduct() {
        final var p = new Product(1L, "P1", 10d, 100, 0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(p));

        final var result = productServiceImpl.getProduct(1L);

        assertEquals("P1", result.getName());
        verify(productRepository).findById(1L);
    }

    @Test
    void getProduct_WhenNotExists_ThrowsException() {
        when(productRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productServiceImpl.getProduct(10L));

        verify(productRepository).findById(10L);
    }

    @Test
    void refill_IncreasesStock() {
        final var p = new Product(1L, "P1", 10d, 100, 0);
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        final var result = productServiceImpl.refill(1L, 5);

        assertEquals(105, result.getStock());
        verify(productRepository).save(p);
    }

    @Test
    void buy_DecreasesStock() {
        final var p = new Product(1L, "P1", 10d, 100, 0);
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        final var result = productServiceImpl.buy(1L, 3);

        assertEquals(97, result.getStock());
        verify(productRepository).save(p);
    }

    @Test
    void buy_WhenNotEnoughStock_ThrowsException() {
        final var p = new Product(1L, "P1", 2d, 100, 0);
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));

        assertThrows(NotEnoughStockException.class,
                () -> productServiceImpl.buy(1L, 105));

        verify(productRepository, never()).save(any());
    }

    @Test
    void getStock_ReturnsCorrectValue() {
        final var p = new Product(1L, "P1", 10d, 100, 0);
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));

        final var stock = productServiceImpl.getProductStock(1L);

        assertEquals(100, stock.get("P1"));
    }
}
