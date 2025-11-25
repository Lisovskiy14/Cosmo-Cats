package com.example.cosmocats.web.mapper;

import com.example.cosmocats.domain.Order;
import com.example.cosmocats.domain.OrderItem;
import com.example.cosmocats.dto.order.OrderDto;
import com.example.cosmocats.dto.order.OrderItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderWebMapper {
    @Mapping(source = "customer.id", target = "customerId")
    OrderDto toOrderDto(Order order);

    @Mapping(source = "product.id", target = "productId")
    OrderItemDto toOrderItemDto(OrderItem orderItem);
}
