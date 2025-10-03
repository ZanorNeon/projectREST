package com.example.projectREST.controller;

import com.example.projectREST.config.SecurityConfig;
import com.example.projectREST.model.CategoryEntity;
import com.example.projectREST.service.CategoryService;
import com.example.projectREST.service.CustomUserDetailsService;
import com.example.projectREST.service.JwtProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@Import(SecurityConfig.class)
@AutoConfigureMockMvc
@WebMvcTest(CategoryController.class)
class CategoryEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;


    @Test
    @WithMockUser(username="kek@lol.com", roles = "ADMIN")
    void getAllCategories_ShouldReturnList() throws Exception {
        CategoryEntity c1 = new CategoryEntity(1L, "Electronics", "electronics", "All electronic items", Instant.now(), Instant.now());
        CategoryEntity c2 = new CategoryEntity(2L, "Books", "books", "All kinds of books", Instant.now(), Instant.now());
        List<CategoryEntity> categories = Arrays.asList(c1, c2);

        Mockito.when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/categories/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size(2)").value(2))
                .andExpect(jsonPath("$[0].name").value("Electronics"))
                .andExpect(jsonPath("$[1].name").value("Books"));
    }

    @Test
    void getCategory_ShouldReturnSingleCategory() throws Exception {
        CategoryEntity c1 = new CategoryEntity(1L, "Electronics", "electronics", "All electronic items", Instant.now(), Instant.now());

        Mockito.when(categoryService.getCategory(1L)).thenReturn(c1);

        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"))
                .andExpect(jsonPath("$.slug").value("electronics"));
    }
}