package com.example.cosmocats.service.exception.conflict;

public class CustomerEmailAlreadyExistsException extends ResourceAlreadyExistsException {
    private static final String CUSTOMER_WITH_EMAIL_ALREADY_EXISTS = "Customer with email '%s' already exists";

    public CustomerEmailAlreadyExistsException(String email) {
        super(String.format(CUSTOMER_WITH_EMAIL_ALREADY_EXISTS, email));
    }
}
