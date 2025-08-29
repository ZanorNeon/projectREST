package com.example.projectREST.controller;

import com.example.projectREST.model.Category;
import com.example.projectREST.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.Instant;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @Test
    void getAllCategories_ShouldReturnList() throws Exception {
        Category c1 = new Category(1L, "Electronics", "electronics", Instant.now(), Instant.now());
        c1.setId(1L);
        c1.setName("Electronics");

        Category c2 = new Category(2L, "Books", "books",Instant.now(),Instant.now());
        c2.setId(2L);
        c2.setName("Books");

        Mockito.when(categoryService.getAllCategories())
                .thenReturn(Arrays.asList(c1, c2));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$[0].name").value("Electronics"))
                .andExpect((ResultMatcher) jsonPath("$[1].name").value("Books"));
    }

}