package com.example.projectREST.service;

import com.example.projectREST.dto.ProductDto;
import com.example.projectREST.mapper.ProductMapper;
import com.example.projectREST.model.CategoryEntity;
import com.example.projectREST.model.ProductEntity;
import com.example.projectREST.repository.CategoryRepository;
import com.example.projectREST.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    public List<ProductEntity> getProducts(BigDecimal minPrice,
                                           BigDecimal maxPrice,
                                           Long categoryId,
                                           Boolean active,
                                           String q,
                                           Pageable pageable) {
        var products = productRepository.findAll(ProductRepository.filter(minPrice, maxPrice, categoryId, active, q), pageable);
        return products.stream().toList();
    }

    public ProductEntity getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public ProductEntity createProduct(ProductDto productDto) {
        CategoryEntity categoryEntity = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        ProductEntity productEntity = productMapper.toEntity(productDto);

        productEntity.setCategoryEntity(categoryEntity);
        productEntity.setCreatedAt(Instant.now());
        productEntity.setUpdatedAt(Instant.now());

        return productRepository.save(productEntity);
    }

    public ProductEntity updateProduct(Long id, ProductDto productDto) {
        ProductEntity productEntity = getProduct(id);

        productEntity.setName(productDto.getName());
        productEntity.setSlug(productDto.getSlug());
        productEntity.setDescription(productDto.getDescription());
        productEntity.setPrice(productDto.getPrice());
        productEntity.setCurrency(productDto.getCurrency());
        productEntity.setStock(productDto.getStock());
        productEntity.setActive(productDto.getActive());
        productEntity.setUpdatedAt(Instant.now());

        if (productDto.getCategoryId() != null) {
            CategoryEntity categoryEntity = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            productEntity.setCategoryEntity(categoryEntity);
        }
        return productEntity;
    }

    public ProductEntity updateStock(Long id, Integer stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock must be >= 0");
        }
        ProductEntity productEntity = getProduct(id);
        productEntity.setStock(stock);
        productEntity.setUpdatedAt(Instant.now());
        return productRepository.save(productEntity);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}

