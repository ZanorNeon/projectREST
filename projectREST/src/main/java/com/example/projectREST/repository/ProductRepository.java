package com.example.projectREST.repository;

import com.example.projectREST.model.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    static Specification<Product> filter(BigDecimal minPrice, BigDecimal maxPrice, Long categoryId, Boolean active, String q) {
        return null;
    }

    Optional<Product> findBySlug(String slug);
}
