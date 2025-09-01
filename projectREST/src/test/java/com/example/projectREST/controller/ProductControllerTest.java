package com.example.projectREST.controller;

import com.example.projectREST.model.Product;
import com.example.projectREST.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @Test
    void getAllProducts_ShouldReturnList() throws Exception {
        Product p1 = new Product(101L, "Phone", "phone", "High-performance phone", BigDecimal.valueOf(500), "USD", 10, true, null, Instant.now(), Instant.now());
        p1.setId(1L);
        p1.setName("Phone");
        p1.setPrice(BigDecimal.valueOf(500));

        Product p2 = new Product(102L, "Laptop", "laptop", "High-performance laptop", BigDecimal.valueOf(500), "USD", 10, true, null, Instant.now(), Instant.now());
        p2.setId(2L);
        p2.setName("Laptop");
        p2.setPrice(BigDecimal.valueOf(1200));
        List<Product> products = Arrays.asList(p1, p2);
        Page<Product> productPage = new PageImpl<>(products);

        Mockito.when(productService.getProducts(
                Mockito.isNull(), // minPrice
                Mockito.isNull(), // maxPrice
                Mockito.isNull(), // categoryId
                Mockito.isNull(), // active
                Mockito.isNull(), // q
                Mockito.any(Pageable.class)
        )).thenReturn(productPage);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.content", hasSize(2)))
                .andExpect((ResultMatcher) jsonPath("$.content[0].name").value("Phone"))
                .andExpect((ResultMatcher) jsonPath("$.content[1].name").value("Laptop"));
    }

}