package com.example.projectREST.service;

import com.example.projectREST.model.CategoryEntity;
import com.example.projectREST.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class CategoryEntityServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private UserDetailsService UserDetailsService;

    private CategoryEntity sampleCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        CategoryEntity sampleCategory = new CategoryEntity(1L, "Electronics", "electronics", "desc",
                Instant.now(), Instant.now());
    }


        @Test
    void getAllCategories_ShouldReturnList() throws Exception {
            List<CategoryEntity> categories = List.of(sampleCategory);
            when(categoryRepository.findAll()).thenReturn(categories);
            List<CategoryEntity> result = categoryRepository.findAll();
            assertEquals(1, result.size());
            verify(categoryRepository, times(1)).findAll();
        }

    @Test
    void getCategory_ShouldReturnSingleCategory() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));
        Optional<CategoryEntity> result = categoryRepository.findById(1L);
        assertNotNull(result);
        assertEquals("Test", result.get());
    }


    @Test
    void deleteCategory_ShouldCallRepository() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        categoryService.deleteCategory(1L);
        verify(categoryRepository).deleteById(1L);
    }

}