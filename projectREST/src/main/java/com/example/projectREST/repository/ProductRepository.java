package com.example.projectREST.repository;

import com.example.projectREST.model.ProductEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {
    static Specification<ProductEntity> filter(BigDecimal minPrice, BigDecimal maxPrice, Long categoryId, Boolean active, String q) {
        return null;
    }

    Optional<ProductEntity> findBySlug(String slug);
}
