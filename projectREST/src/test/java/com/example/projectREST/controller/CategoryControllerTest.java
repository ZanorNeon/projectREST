package com.example.projectREST.controller;

import com.example.projectREST.model.Category;
import com.example.projectREST.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    void getAllCategories_ShouldReturnList() throws Exception {
        Category c1 = new Category(1L, "Electronics", "electronics", "All electronic items", Instant.now(), Instant.now());
        Category c2 = new Category(2L, "Books", "books", "All kinds of books", Instant.now(), Instant.now());
        List<Category> categories = Arrays.asList(c1, c2);

        Mockito.when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Electronics"))
                .andExpect(jsonPath("$[1].name").value("Books"));
    }

    @Test
    void getCategory_ShouldReturnSingleCategory() throws Exception {
        Category c1 = new Category(1L, "Electronics", "electronics", "All electronic items", Instant.now(), Instant.now());

        Mockito.when(categoryService.getCategory(1L)).thenReturn(c1);

        mockMvc.perform(get("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"))
                .andExpect(jsonPath("$.slug").value("electronics"));
    }
}