package com.example.cosmocats.service;

import com.example.cosmocats.common.OrderStatus;
import com.example.cosmocats.domain.Order;
import com.example.cosmocats.dto.order.OrderRequestDto;
import com.example.cosmocats.dto.order.UpdateOrderStatusRequestDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<Order> getAllOrders();
    List<Order> getAllOrdersByStatus(OrderStatus orderStatus);
    List<Order> getAllOrdersByCustomerId(UUID id);
    Order getOrderByNaturalId(String id);
    Order placeOrder(OrderRequestDto orderRequestDto);
    Order updateOrderStatus(String orderNumber, UpdateOrderStatusRequestDto updateOrderStatusRequestDto);
    void deleteOrderByNaturalId(String id);
}
