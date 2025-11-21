package com.example.cosmocats.web;

import com.example.cosmocats.domain.Order;
import com.example.cosmocats.dto.order.OrderDto;
import com.example.cosmocats.dto.order.OrderListDto;
import com.example.cosmocats.dto.order.OrderRequestDto;
import com.example.cosmocats.dto.order.UpdateOrderStatusRequestDto;
import com.example.cosmocats.service.OrderService;
import com.example.cosmocats.web.mapper.OrderWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderWebMapper orderWebMapper;

    @GetMapping
    public ResponseEntity<OrderListDto> getAllOrders(
            @RequestParam(name = "customerId", required = false) UUID customerId
    ) {
        List<Order> orders;
        if (customerId != null) {
            orders = orderService.getAllOrdersByCustomerId(customerId);
        } else {
            orders = orderService.getAllOrders();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new OrderListDto(
                        orders.stream()
                                .map(orderWebMapper::toOrderDto)
                                .toList()
                        )
                );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable UUID orderId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderWebMapper.toOrderDto(
                        orderService.getOrderById(orderId)));
    }

    @PostMapping
    public ResponseEntity<OrderDto> placeOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderWebMapper.toOrderDto(
                        orderService.placeOrder(orderRequestDto)));
    }

    @PatchMapping
    public ResponseEntity<OrderDto> updateOrderStatus(
            @RequestBody UpdateOrderStatusRequestDto updateOrderStatusRequestDto
    ) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderWebMapper.toOrderDto(
                        orderService.updateOrderStatus(updateOrderStatusRequestDto)));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable UUID orderId) {
        orderService.deleteOrderById(orderId);
        return ResponseEntity.noContent()
                .build();
    }
}
