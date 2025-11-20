package com.example.cosmocats.service.impl;

import com.example.cosmocats.domain.Category;
import com.example.cosmocats.repository.CategoryRepository;
import com.example.cosmocats.repository.entity.CategoryEntity;
import com.example.cosmocats.service.CategoryService;
import com.example.cosmocats.service.exception.CategoryNotFoundException;
import com.example.cosmocats.service.mapper.CategoryEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryEntityMapper categoryEntityMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryEntityMapper::toCategory)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(UUID id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return categoryEntityMapper.toCategory(categoryEntity);
    }

    @Override
    public Category getCategoryByName(String name) {
        CategoryEntity categoryEntity = categoryRepository.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException(name));
        return categoryEntityMapper.toCategory(categoryEntity);
    }

    @Override
    @Transactional
    public Category saveCategory(Category category) {
        CategoryEntity categoryEntity = categoryEntityMapper.toCategoryEntity(category);
        return categoryEntityMapper.toCategory(
                categoryRepository.save(categoryEntity));
    }

    @Override
    @Transactional
    public void deleteCategoryById(UUID id) {
        categoryRepository.deleteById(id);
    }
}
