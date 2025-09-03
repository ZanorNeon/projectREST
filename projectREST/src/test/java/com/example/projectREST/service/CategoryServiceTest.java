package com.example.projectREST.service;

import com.example.projectREST.model.Category;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.HashSet;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    void getAllCategories_ShouldReturnList() throws Exception {
        Category c1 = new Category(1L, "Electronics", "electronics", "desc",
                Instant.now(), Instant.now(), new HashSet<>());

        Mockito.when(categoryService.getAllCategories()).thenReturn(List.of(c1));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electronics"));
    }

    @Test
    void getCategory_ShouldReturnSingleCategory() throws Exception {
        Category c1 = new Category(1L, "Books", "books", "desc",
                Instant.now(), Instant.now(), new HashSet<>());

        Mockito.when(categoryService.getCategory(1L)).thenReturn(c1);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Books"));
    }
}

@Test
void deleteCategory_ShouldCallRepository() {
    CategoryService.deleteCategory(1L);
    verify(CategoryRepository, times(1)).deleteById(1L);
}

