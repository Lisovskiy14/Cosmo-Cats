package com.example.cosmocats.web;

import com.example.cosmocats.domain.Category;
import com.example.cosmocats.dto.category.CategoryListDto;
import com.example.cosmocats.dto.category.CategoryRequestDto;
import com.example.cosmocats.service.CategoryService;
import com.example.cosmocats.web.mapper.CategoryWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryWebMapper categoryWebMapper;

    @GetMapping
    public ResponseEntity<Object> getAllCategories() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CategoryListDto(
                        categoryService.getAllCategories().stream()
                                .map(categoryWebMapper::toCategoryDto)
                                .toList()));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Object> getCategoryById(@PathVariable UUID categoryId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(categoryWebMapper.toCategoryDto(
                        categoryService.getCategoryById(categoryId)));
    }

    @PostMapping
    public ResponseEntity<Object> saveCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        Category category = categoryWebMapper.toCategory(categoryRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(categoryWebMapper.toCategoryDto(
                        categoryService.saveCategory(category)));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Object> deleteCategoryById(@PathVariable UUID categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return ResponseEntity.noContent()
                .build();
    }
}
