package com.example.cosmocats.dto.category;

import lombok.Value;

import java.util.List;

@Value
public class CategoryListDto {
    List<CategoryDto> categories;
}
