package com.example.projectREST.service;

import com.example.projectREST.model.Category;
import com.example.projectREST.model.Product;
import com.example.projectREST.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        Category category = new Category(1L, "Electronics", "electronics", "aaaaaaaa", Instant.now(), Instant.now());

        product = new Product(1L, "Phone", "phone", "Smartphone", BigDecimal.valueOf(1000), "USD", 10, true, null, Instant.now(), Instant.now());
    }

    @Test
    void getProductById_ShouldReturnProduct() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product found = productService.getProduct(1L);

        assertNotNull(found);
        assertEquals("Phone", found.getName());
    }

    @Test
    void getProductById_ShouldThrowException_WhenNotFound() {
        Mockito.when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getProduct(99L));
    }

    @Test
    void createProduct_ShouldSaveProduct() {
        Mockito.when(productRepository.save(any(Product.class))).thenReturn(product);

        Product saved = productService.createProduct(product);

        assertNotNull(saved);
        assertEquals("Phone", saved.getName());
        Mockito.verify(productRepository, times(1)).save(product);
    }
}