package com.example.cosmocats.service.exception.notFound;

public class CustomerNotFoundException extends ResourceNotFoundException {
    private static final String CUSTOMER_WITH_ID_NOT_FOUND = "Customer with id '%s' not found";

    public CustomerNotFoundException(String id) {
        super(String.format(CUSTOMER_WITH_ID_NOT_FOUND, id));
    }
}
