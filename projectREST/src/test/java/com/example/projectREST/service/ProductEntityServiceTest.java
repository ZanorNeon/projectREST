package com.example.projectREST.service;

import com.example.projectREST.dto.ProductDto;
import com.example.projectREST.model.CategoryEntity;
import com.example.projectREST.model.ProductEntity;
import com.example.projectREST.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@AutoConfigureMockMvc
class ProductEntityServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductEntity productEntity;


    @BeforeEach
    void setUp() {
        CategoryEntity categoryEntity = new CategoryEntity(1L, "Electronics", "electronics", "aaaaaaaa", Instant.now(), Instant.now());

        productEntity = new ProductEntity(1L, "Phone", "phone", "Smartphone", BigDecimal.valueOf(1000), "USD", 10, true, Instant.now(), Instant.now());
    }

    @Test
    void getProductById_ShouldReturnProduct() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));

        ProductEntity found = productService.getProduct(1L);

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
        Mockito.when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

        ProductEntity saved = productService.createProduct(new ProductDto(1L, "Phone", "phone", "Smartphone", BigDecimal.valueOf(1000), "USD", 10, true, Instant.now(), Instant.now(), 1L));

        assertNotNull(saved);
        assertEquals("Phone", saved.getName());
        Mockito.verify(productRepository, times(1)).save(productEntity);
    }
}