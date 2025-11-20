package com.example.cosmocats.service.exception;

public class CategoryNotFoundException extends ResourceNotFoundException {
    private static final String CATEGORY_WITH_ID_NOT_FOUND = "Category with id '%s' not found";

    public CategoryNotFoundException(String id) {
        super(String.format(CATEGORY_WITH_ID_NOT_FOUND, id));
    }
}
