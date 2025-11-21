package com.example.cosmocats.service;

import com.example.cosmocats.domain.Order;
import com.example.cosmocats.dto.order.OrderRequestDto;
import com.example.cosmocats.dto.order.UpdateOrderStatusRequestDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<Order> getAllOrders();
    List<Order> getAllOrdersByCustomerId(UUID id);
    Order getOrderById(UUID id);
    Order placeOrder(OrderRequestDto orderRequestDto);
    Order updateOrderStatus(UpdateOrderStatusRequestDto updateOrderStatusRequestDto);
    void deleteOrderById(UUID id);
}
