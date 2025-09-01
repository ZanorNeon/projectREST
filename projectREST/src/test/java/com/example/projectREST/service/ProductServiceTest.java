package com.example.projectREST.service;

import com.example.projectREST.model.Product;
import com.example.projectREST.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product createExampleProduct() {
        return new Product(1L, "Laptop", "laptop", "High-performance laptop", new BigDecimal("999.99"), "USD", 10, true, null, Instant.now(), Instant.now());
    }

    @Test
    void createProduct_ShouldSaveProduct() {
        Product product = createExampleProduct();

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct(product);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        assertEquals(new BigDecimal("999.99"), result.getPrice());

        verify(productRepository, times(1)).save(product);
    }

    @Test
    void getProductById_ShouldReturnProduct() {
        Product product = createExampleProduct();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProduct(1L);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        assertEquals("USD", result.getCurrency());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductById_ShouldThrowIfNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productService.getProduct(99L));

        assertEquals("Product not found with id 99", exception.getMessage());
        verify(productRepository, times(1)).findById(99L);
    }
}