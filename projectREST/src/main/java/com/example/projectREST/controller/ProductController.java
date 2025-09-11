package com.example.projectREST.controller;

import com.example.projectREST.dto.ProductDto;
import com.example.projectREST.model.ProductEntity;
import com.example.projectREST.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Page<ProductEntity> listProducts(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String q,
            Pageable pageable) {
        return productService.getProducts(minPrice, maxPrice, categoryId, active, q, pageable);
    }

    @GetMapping("/{id}")
    public ProductEntity getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ProductEntity createProduct(@RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProductEntity updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        return productService.updateProduct(id, productDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/stock")
    public ProductEntity updateStock(@PathVariable Long id, @RequestParam Integer stock) {
        return productService.updateStock(id, stock);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
