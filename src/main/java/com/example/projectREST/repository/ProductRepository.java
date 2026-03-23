package com.example.projectREST.repository;

import com.example.projectREST.model.ProductEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {

        static Specification<ProductEntity> filter(
                BigDecimal minPrice,
                BigDecimal maxPrice,
                Long categoryId,
                Boolean active,
                String q
        ) {
            return (root, query, cb) -> {
                Predicate predicate = cb.conjunction();

                if (minPrice != null) {
                    predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("price"), minPrice));
                }
                if (maxPrice != null) {
                    predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("price"), maxPrice));
                }

                if (categoryId != null) {
                    predicate = cb.and(predicate, cb.equal(root.get("categoryEntity").get("id"), categoryId));
                }

                if (active != null) {
                    predicate = cb.and(predicate, cb.equal(root.get("active"), active));
                }

                if (q != null && !q.isBlank()) {
                    String likeQuery = "%" + q.toLowerCase() + "%";
                    predicate = cb.and(predicate,
                            cb.or(
                                    cb.like(cb.lower(root.get("name")), likeQuery),
                                    cb.like(cb.lower(root.get("description")), likeQuery)
                            ));
                }

                return predicate;
            };
        }
    }
