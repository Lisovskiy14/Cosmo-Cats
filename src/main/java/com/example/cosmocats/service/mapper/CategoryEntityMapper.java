package com.example.cosmocats.service.mapper;

import com.example.cosmocats.domain.Category;
import com.example.cosmocats.repository.entity.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryEntityMapper {
    Category toCategory(CategoryEntity categoryEntity);
    CategoryEntity toCategoryEntity(Category category);
}
