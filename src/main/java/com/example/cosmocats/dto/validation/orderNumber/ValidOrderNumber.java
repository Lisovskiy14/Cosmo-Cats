package com.example.cosmocats.dto.validation.orderNumber;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Pattern(
        regexp = "^ORD-[0-9]{2}-[A-Z0-9]{6}$",
        message = "Order number must follow format ORD-YY-XXXXXX (e.g. ORD-25-YT3B9Z)"
)
public @interface ValidOrderNumber {
    String message() default "Invalid order number format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
