package com.example.projectREST.service;

import com.example.projectREST.model.Category;
import com.example.projectREST.model.Product;
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

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<Product> getProducts(BigDecimal minPrice,
                                         BigDecimal maxPrice,
                                         Long categoryId,
                                         Boolean active,
                                         String q,
                                         Pageable pageable) {
            return productRepository.findAll(ProductRepository.filter(minPrice, maxPrice, categoryId, active, q), (org.springframework.data.domain.Pageable) pageable);
        }

        public Product getProduct(Long id) {
            return productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
        }

        public Product createProduct(Product product) {
            Category category = categoryRepository.findById(product.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
            return productRepository.save(product);
        }

        public Product updateProduct(Long id, Product updated) {
            Product product = getProduct(id);
            product.setName(updated.getName());
            product.setSlug(updated.getSlug());
            product.setDescription(updated.getDescription());
            product.setPrice(updated.getPrice());
            product.setCurrency(updated.getCurrency());
            product.setStock(updated.getStock());
            product.setActive(updated.getActive());
            product.setCategory(updated.getCategory());
            product.setUpdatedAt(updated.getUpdatedAt());
            return productRepository.save(product);
        }

        public Product updateStock(Long id, Integer stock) {
            Product product = getProduct(id);
            product.setStock(stock);
            return productRepository.save(product);
        }

        public void deleteProduct(Long id) {
            productRepository.deleteById(id);
        }
    }

