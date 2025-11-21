package com.example.cosmocats.service.mapper;

import com.example.cosmocats.domain.Order;
import com.example.cosmocats.domain.OrderItem;
import com.example.cosmocats.repository.entity.OrderEntity;
import com.example.cosmocats.repository.entity.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {CustomerEntityMapper.class, ProductEntityMapper.class}
)
public interface OrderEntityMapper {
    Order toOrder(OrderEntity orderEntity);
    OrderEntity toOrderEntity(Order order);

    @Mapping(target = "order", ignore = true)
    OrderItem toOrderItem(OrderItemEntity orderItemEntity);

    @Mapping(target = "order", ignore = true)
    OrderItemEntity toOrderItemEntity(OrderItem orderItem);
}
