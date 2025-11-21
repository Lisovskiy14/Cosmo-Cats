package com.example.cosmocats.service.exception.notFound;


public class ProductNotFoundException extends ResourceNotFoundException {
    private static final String PRODUCT_WITH_ID_NOT_FOUND = "Product with id '%s' not found";

    public ProductNotFoundException(String id) {
        super(String.format(PRODUCT_WITH_ID_NOT_FOUND, id));
    }
}
