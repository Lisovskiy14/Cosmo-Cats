package com.example.cosmocats.dto.order;

import lombok.Value;

import java.util.List;

@Value
public class OrderListDto {
    List<OrderDto> orders;
}
