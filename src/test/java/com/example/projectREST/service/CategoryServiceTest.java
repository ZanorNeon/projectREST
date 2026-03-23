package com.example.projectREST.service;

import com.example.projectREST.dto.CategoryDto;
import com.example.projectREST.model.CategoryEntity;
import com.example.projectREST.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryDto sampleCategory;

    @BeforeEach
    void setUp() {
        sampleCategory = new CategoryDto(1L, "Electronics", "electronics", "desc");
    }

    @Test
    void getAllCategories_ShouldReturnList() {
        List<CategoryEntity> categories = List.of(sampleCategory);

        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryEntity> result = categoryService.getAllCategories();

        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategory_ShouldReturnSingleCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.<CategoryEntity>of(sampleCategory));

        CategoryEntity result = categoryService.getCategory(1L);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void updateCategory_ShouldUpdateFields() {
        CategoryDto updatedCategory = new CategoryDto(
                null, "New Name", "new-slug", "New description");

        Mockito.when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(sampleCategory));
        Mockito.when(categoryRepository.save(any(CategoryEntity.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        CategoryEntity result = categoryService.updateCategory(1L, (CategoryDto) updatedCategory);

        assertEquals("New Name", result.getName());
        assertEquals("new-slug", result.getSlug());
        assertEquals("New description", result.getDescription());
        Mockito.verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
    }

    @Test
    void deleteCategory_ShouldCallRepository() {
        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }
}