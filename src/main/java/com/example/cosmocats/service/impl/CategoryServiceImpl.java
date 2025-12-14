package com.example.cosmocats.service.impl;

import com.example.cosmocats.domain.Category;
import com.example.cosmocats.repository.CategoryRepository;
import com.example.cosmocats.repository.entity.CategoryEntity;
import com.example.cosmocats.service.CategoryService;
import com.example.cosmocats.service.exception.notFound.CategoryNotFoundException;
import com.example.cosmocats.service.mapper.CategoryEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryEntityMapper categoryEntityMapper;

    @Override
    @PreAuthorize("hasAnyRole('API', 'ADMIN')")
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryEntityMapper::toCategory)
                .toList();
    }

    @Override
    @PreAuthorize("hasAnyRole('API', 'ADMIN')")
    @Transactional(readOnly = true)
    public Category getCategoryById(UUID id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return categoryEntityMapper.toCategory(categoryEntity);
    }

    @Override
    @PreAuthorize("hasAnyRole('API', 'ADMIN')")
    @Transactional(readOnly = true)
    public Category getCategoryByName(String name) {
        CategoryEntity categoryEntity = categoryRepository.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException(name));
        return categoryEntityMapper.toCategory(categoryEntity);
    }

    @Override
    @PreAuthorize("hasAnyRole('API', 'ADMIN')")
    @Transactional
    public Category saveCategory(Category category) {
        CategoryEntity categoryEntity = categoryEntityMapper.toCategoryEntity(category);
        CategoryEntity savedCategoryEntity = categoryRepository.save(categoryEntity);
        log.info("New category with id '{}' has been saved", savedCategoryEntity.getId());
        return categoryEntityMapper.toCategory(savedCategoryEntity);
    }

    @Override
    @PreAuthorize("hasAnyRole('API', 'ADMIN')")
    @Transactional
    public void deleteCategoryById(UUID id) {
        categoryRepository.deleteById(id);
        log.info("Category with id '{}' has been deleted", id);
    }
}
