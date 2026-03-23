package com.example.projectREST.service;

import com.example.projectREST.dto.CategoryDto;
import com.example.projectREST.mapper.CategoryMapper;
import com.example.projectREST.model.CategoryEntity;
import com.example.projectREST.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    public CategoryEntity getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public CategoryEntity createCategory(CategoryDto categoryDto) {
        CategoryEntity categoryEntity = categoryMapper.toEntity(categoryDto);
        return categoryRepository.save(categoryEntity);
    }

    public CategoryEntity updateCategory(Long id, CategoryDto updated) {

        CategoryEntity categoryEntity = getCategory(id);

        categoryEntity.setName(updated.getName());
        categoryEntity.setSlug(updated.getSlug());
        categoryEntity.setDescription(updated.getDescription());
        categoryEntity.setUpdatedAt(Instant.now());

        return categoryRepository.save(categoryEntity);
    }


    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
