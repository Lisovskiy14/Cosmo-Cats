package com.example.cosmocats.service.exception;

public class ProductNameAlreadyExistsException extends RuntimeException {
    private static final String PRODUCT_NAME_ALREADY_EXISTS = "Product name '%s' already exists within category id '%s'";

    public ProductNameAlreadyExistsException(String name, String categoryId) {
        super(String.format(PRODUCT_NAME_ALREADY_EXISTS, name, categoryId));
    }
}
