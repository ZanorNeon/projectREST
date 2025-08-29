package com.example.projectREST.controller;

import com.example.projectREST.model.Product;
import com.example.projectREST.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;

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
        Product p1 = new Product(101L, "Phone", "phone", "High-performance phone", "999.99", "USD", 10, true, "Electronics", Instant.now(), Instant.now());
        p1.setId(1L);
        p1.setName("Phone");
        p1.setPrice(BigDecimal.valueOf(500));

        Product p2 = new Product(102L, "Laptop", "laptop", "High-performance laptop", "969.69", "USD", 10, true, "Electronics", Instant.now(), Instant.now());
        p2.setId(2L);
        p2.setName("Laptop");
        p2.setPrice(BigDecimal.valueOf(1200));

        Mockito.when(productService.getProducts()
                .thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$[0].name").value("Phone"))
                .andExpect((ResultMatcher) jsonPath("$[1].name").value("Laptop"));
    }

}