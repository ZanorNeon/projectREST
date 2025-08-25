package com.example.projectREST.service;

import com.example.projectREST.model.Category;
import com.example.projectREST.repository.CategoryRepository;

import java.util.List;

public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category updated) {
        Category category = getCategory(id);
        category.setName(updated.getName());
        category.setSlug(updated.getSlug());
        category.setDescription(updated.getDescription());
        category.setUpdatedAt(updated.getUpdatedAt());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
