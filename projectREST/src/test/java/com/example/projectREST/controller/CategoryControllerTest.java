package com.example.projectREST.controller;

import com.example.projectREST.model.Category;
import com.example.projectREST.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @Test
    void getAllCategories_ShouldReturnList() throws Exception {
        Category c1 = new Category(1L, "Electronics", "electronics", "Electro lol", Instant.now(), Instant.now());

        Category c2 = new Category(2L, "Books", "books", "Nerd power", Instant.now(), Instant.now());
        List<Category> categories = Arrays.asList(c1, c2);

        Mockito.when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.size()").value(2))
                .andExpect((ResultMatcher) jsonPath("$[0].name").value("Electronics"))
                .andExpect((ResultMatcher) jsonPath("$[1].name").value("Books"));
    }

}