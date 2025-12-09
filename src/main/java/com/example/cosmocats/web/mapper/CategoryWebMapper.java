package com.example.cosmocats.web.mapper;

import com.example.cosmocats.domain.Category;
import com.example.cosmocats.dto.category.CategoryDto;
import com.example.cosmocats.dto.category.CategoryRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryWebMapper {
    CategoryDto toCategoryDto(Category category);
    Category toCategory(CategoryRequestDto categoryRequestDto);
}
