package com.example.projectREST.service;

import com.example.projectREST.model.CategoryEntity;
import com.example.projectREST.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryEntityServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;
    
    private CategoryRepository categoryRepository;

    @Test
    void getAllCategories_ShouldReturnList() throws Exception {
        CategoryEntity c1 = new CategoryEntity(1L, "Electronics", "electronics", "desc",
                Instant.now(), Instant.now());

        Mockito.when(categoryService.getAllCategories()).thenReturn(List.of(c1));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$[0].name").value("Electronics"));
    }

    @Test
    void getCategory_ShouldReturnSingleCategory() throws Exception {
        CategoryEntity c1 = new CategoryEntity(1L, "Books", "books", "desc",
                Instant.now(), Instant.now());

        Mockito.when(categoryService.getCategory(1L)).thenReturn(c1);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.name").value("Books"));
    }


    @Test
    void deleteCategory_ShouldCallRepository() {
        categoryService.deleteCategory(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }

}