package com.example.cosmocats.service.exception.conflict;

public class CustomerPhoneNumberAlreadyExistsException extends ResourceAlreadyExistsException {
    private static final String CUSTOMER_WITH_PHONE_ALREADY_EXISTS = "Customer with phone number '%s' already exists";

    public CustomerPhoneNumberAlreadyExistsException(String phoneNumber) {
        super(String.format(CUSTOMER_WITH_PHONE_ALREADY_EXISTS, phoneNumber));
    }
}
