package com.example.cosmocats.domain;

import com.example.cosmocats.common.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class Order {
    UUID id;
    Customer customer;
    BigDecimal totalPrice;
    OrderStatus status;
    LocalDateTime createdAt;
    List<OrderItem> items = new ArrayList<>();

    void calculateTotalPrice() {
        this.totalPrice = items.stream()
                .map((item) -> item
                        .getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
