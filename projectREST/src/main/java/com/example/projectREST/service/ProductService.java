package com.example.projectREST.service;

import com.example.projectREST.dto.ProductDto;
import com.example.projectREST.mapper.ProductMapper;
import com.example.projectREST.model.CategoryEntity;
import com.example.projectREST.model.ProductEntity;
import com.example.projectREST.repository.CategoryRepository;
import com.example.projectREST.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.math.BigDecimal;

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

    public Page<ProductEntity> getProducts(BigDecimal minPrice,
                                           BigDecimal maxPrice,
                                           Long categoryId,
                                           Boolean active,
                                           String q,
                                           Pageable pageable) {
        return productRepository.findAll(ProductRepository.filter(minPrice, maxPrice, categoryId, active, q), (org.springframework.data.domain.Pageable) pageable);
    }

    public ProductEntity getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public ProductEntity createProduct(ProductDto productDto) {
        CategoryEntity categoryEntity = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        ProductEntity productEntity = productMapper.toEntity(productDto); // TODO

        productEntity.setCategoryId(categoryEntity);
        return productRepository.save(productEntity);
    }

    public ProductEntity updateProduct(Long id, ProductDto productDto) { // TODO
        ProductEntity productEntity = getProduct(id);
        productEntity.setName(updated.getName());
        productEntity.setSlug(updated.getSlug());
        productEntity.setDescription(updated.getDescription());
        productEntity.setPrice(updated.getPrice());
        productEntity.setCurrency(updated.getCurrency());
        productEntity.setStock(updated.getStock());
        productEntity.setActive(updated.getActive());
        productEntity.setCategoryId(updated.getCategoryId());
        productEntity.setUpdatedAt(updated.getUpdatedAt());
        return productRepository.save(productEntity);
    }

    public ProductEntity updateStock(Long id, Integer stock) {
        ProductEntity productEntity = getProduct(id);
        productEntity.setStock(stock);
        return productRepository.save(productEntity);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}

