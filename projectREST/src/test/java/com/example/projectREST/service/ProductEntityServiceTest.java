package com.example.projectREST.service;

import com.example.projectREST.dto.ProductDto;
import com.example.projectREST.mapper.ProductMapper;
import com.example.projectREST.model.CategoryEntity;
import com.example.projectREST.model.ProductEntity;
import com.example.projectREST.repository.CategoryRepository;
import com.example.projectREST.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ProductEntityServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private ProductEntity sampleProduct;
    private CategoryEntity sampleCategory;

    @BeforeEach
    void setUp() {
        sampleCategory = new CategoryEntity(
                1L, "Electronics", "electronics", "aaaaaaaa",
                Instant.now(), Instant.now()
        );

        sampleProduct = new ProductEntity(
                1L, "Phone", "phone", "Smartphone",
                BigDecimal.valueOf(1000), "USD", 10, true,
                Instant.now(), Instant.now()
        );
        sampleProduct.setCategoryEntity(sampleCategory);
    }

    @Test
    void getProductById_ShouldReturnProduct() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

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
            ProductDto dto = new ProductDto(1L, "Phone", "phone", "Smartphone", BigDecimal.valueOf(1000), "USD", 10, true, Instant.now(), Instant.now(), 1L);

            Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));
            Mockito.when(productMapper.toEntity(dto)).thenReturn(sampleProduct);
            Mockito.when(productRepository.save(any(ProductEntity.class))).thenReturn(sampleProduct);

            ProductEntity saved = productService.createProduct(dto);

            assertNotNull(saved);
            assertEquals("Phone", saved.getName());
            verify(productRepository, times(1)).save(any(ProductEntity.class));
            verify(categoryRepository, times(1)).findById(1L);
            verify(productMapper, times(1)).toEntity(dto);
        }
    }