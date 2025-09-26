package com.example.projectREST.controller;

import com.example.projectREST.model.CategoryEntity;
import com.example.projectREST.model.ProductEntity;
import com.example.projectREST.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ProductController.class)
class ProductEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    private CategoryEntity categoryEntity;

    @BeforeEach
    void setup() {
        categoryEntity = new CategoryEntity(1L, "Electronics", "electronics", "desc",
                Instant.now(), Instant.now());
    }

    @Test
    void getAllProducts_ShouldReturnList() throws Exception {
        ProductEntity p = new ProductEntity(1L, "Phone", "phone", "Smartphone",
                BigDecimal.valueOf(500), "USD", 10, true, Instant.now(), Instant.now());

        Mockito.when(productService.getProducts(null, null, null, null, null, (Pageable) PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(List.of(p)));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.content[0].name").value("Phone"));
    }
}