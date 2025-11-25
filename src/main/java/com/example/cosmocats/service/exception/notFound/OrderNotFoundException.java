package com.example.cosmocats.service.exception.notFound;

public class OrderNotFoundException extends ResourceNotFoundException {
    private static final String ORDER_WITH_ID_NOT_FOUND = "Order with number '%s' not found";

    public OrderNotFoundException(String orderNumber) {
        super(String.format(ORDER_WITH_ID_NOT_FOUND, orderNumber));
    }
}
