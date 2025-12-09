package com.example.cosmocats.service.exception.notFound;

import java.util.UUID;

public class CategoryNotFoundException extends ResourceNotFoundException {
    private static final String CATEGORY_WITH_ID_NOT_FOUND = "Category with id '%s' not found";
    private static final String CATEGORY_WITH_NAME_NOT_FOUND = "Category with name '%s' not found";

    public CategoryNotFoundException(UUID id) {
        super(String.format(CATEGORY_WITH_ID_NOT_FOUND, id));
    }

    public CategoryNotFoundException(String name) {
        super(String.format(CATEGORY_WITH_NAME_NOT_FOUND, name));
    }
}
