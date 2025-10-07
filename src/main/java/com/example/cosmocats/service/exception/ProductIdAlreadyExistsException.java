package com.example.cosmocats.service.exception;

public class ProductIdAlreadyExistsException extends RuntimeException {
    private static final String PRODUCT_WITH_ID_ALREADY_EXISTS = "Product with id '%s' already exists";

    public ProductIdAlreadyExistsException(String id) {
        super(String.format(PRODUCT_WITH_ID_ALREADY_EXISTS, id));
    }
}
