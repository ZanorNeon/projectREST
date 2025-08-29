package com.example.projectREST.service;

import com.example.projectREST.model.Category;
import com.example.projectREST.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCategory_ShouldSaveCategory() {
        Category category = new Category(1L, "Electronics", "electronics", Instant.now(), Instant.now());
        category.setName("Electronics");

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.createCategory(category);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void getCategoryById_ShouldReturnCategory() {
        Category category = new Category(1L, "Books", "books",Instant.now(),Instant.now());
        category.setId(1L);
        category.setName("Books");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategory(1L);

        assertNotNull(result);
        assertEquals("Books", result.getName());
    }

    @Test
    void deleteCategory_ShouldCallRepository() {
        categoryService.deleteCategory(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }

}