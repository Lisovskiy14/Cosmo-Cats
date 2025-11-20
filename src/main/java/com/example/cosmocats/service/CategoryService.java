package com.example.cosmocats.service;

import com.example.cosmocats.domain.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(UUID id);
    Category saveCategory(Category category);
    void deleteCategoryById(UUID id);
}
