package com.example.projectREST.controller;

import com.example.projectREST.dto.ProductDto;
import com.example.projectREST.model.ProductEntity;
import com.example.projectREST.service.JwtProvider;
import com.example.projectREST.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserDetailsService userDetailsService;


    private final ProductEntity sampleProduct = new ProductEntity(
            1L, "Phone", "phone", "Smartphone",
            BigDecimal.valueOf(500), "USD", 10, true,
            Instant.now(), Instant.now()
    );

    @Test
    void getAllProducts_ShouldReturnList() throws Exception {
        Mockito.when(productService.getProducts(
                        any(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(sampleProduct)));

        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(1)))
                .andExpect(jsonPath("$.content[0].name", is("Phone")));
    }

    @Test
    void getProductById_ShouldReturnProduct() throws Exception {
        Mockito.when(productService.getProduct(1L)).thenReturn(sampleProduct);

        mockMvc.perform(get("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Phone")))
                .andExpect(jsonPath("$.price", is(500)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        ProductDto dto = new ProductDto(
                1L, "Phone", "phone", "Smartphone",
                BigDecimal.valueOf(500), "USD", 10, true,
                Instant.now(), Instant.now(), 1L
        );

        Mockito.when(productService.createProduct(any(ProductDto.class))).thenReturn(sampleProduct);

        mockMvc.perform(post("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 1,
                                  "name": "Phone",
                                  "slug": "phone",
                                  "description": "Smartphone",
                                  "price": 500,
                                  "currency": "USD",
                                  "stock": 10,
                                  "active": true,
                                  "categoryId": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Phone")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateProduct_ShouldReturnUpdatedProduct() throws Exception {
        ProductDto updatedDto = new ProductDto(
                1L, "Updated", "updated", "New desc",
                BigDecimal.valueOf(1000), "USD", 5, true,
                Instant.now(), Instant.now(), 1L
        );

        ProductEntity updatedEntity = new ProductEntity(
                1L, "Updated", "updated", "New desc",
                BigDecimal.valueOf(1000), "USD", 5, true,
                Instant.now(), Instant.now()
        );

        Mockito.when(productService.updateProduct(eq(1L), any(ProductDto.class)))
                .thenReturn(updatedEntity);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 1,
                                  "name": "Updated",
                                  "slug": "updated",
                                  "description": "New desc",
                                  "price": 1000,
                                  "currency": "USD",
                                  "stock": 5,
                                  "active": true,
                                  "categoryId": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated")))
                .andExpect(jsonPath("$.stock", is(5)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateStock_ShouldReturnUpdatedProduct() throws Exception {
        ProductEntity updated = new ProductEntity(
                1L, "Phone", "phone", "Smartphone",
                BigDecimal.valueOf(500), "USD", 20, true,
                Instant.now(), Instant.now()
        );

        Mockito.when(productService.updateStock(1L, 20)).thenReturn(updated);

        mockMvc.perform(patch("/api/products/1/stock?stock=20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock", is(20)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(productService).deleteProduct(1L);
    }
}
