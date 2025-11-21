package com.example.cosmocats.dto.validation.orderStatus;

import com.example.cosmocats.common.OrderStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OrderStatusValidator implements ConstraintValidator<ValidOrderStatus, String> {

    @Override
    public boolean isValid(String orderStatus, ConstraintValidatorContext context) {
        if (orderStatus == null) {
            return false;
        }

        try {
            OrderStatus.valueOf(orderStatus);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
