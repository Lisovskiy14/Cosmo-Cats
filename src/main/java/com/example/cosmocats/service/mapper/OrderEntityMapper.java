package com.example.cosmocats.service.mapper;

import com.example.cosmocats.domain.Order;
import com.example.cosmocats.domain.OrderItem;
import com.example.cosmocats.repository.entity.OrderEntity;
import com.example.cosmocats.repository.entity.OrderItemEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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

    @AfterMapping
    default void linkOrderToItems(@MappingTarget OrderEntity orderEntity) {
        if (orderEntity.getItems() != null) {
            orderEntity.getItems().forEach(item -> item.setOrder(orderEntity));
        }
    }
}
