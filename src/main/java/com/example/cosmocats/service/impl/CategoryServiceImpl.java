package com.example.cosmocats.service.impl;

import com.example.cosmocats.domain.Category;
import com.example.cosmocats.repository.CategoryRepository;
import com.example.cosmocats.repository.entity.CategoryEntity;
import com.example.cosmocats.service.CategoryService;
import com.example.cosmocats.service.exception.CategoryNotFoundException;
import com.example.cosmocats.service.mapper.CategoryServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryServiceMapper categoryServiceMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryServiceMapper::toCategory)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(UUID id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id.toString()));
        return categoryServiceMapper.toCategory(categoryEntity);
    }

    @Override
    @Transactional
    public Category saveCategory(Category category) {
        CategoryEntity categoryEntity = categoryServiceMapper.toCategoryEntity(category);
        return categoryServiceMapper.toCategory(
                categoryRepository.save(categoryEntity));
    }

    @Override
    @Transactional
    public void deleteCategoryById(UUID id) {
        categoryRepository.deleteById(id);
    }
}
