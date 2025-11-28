package com.example.cosmocats.web;

import com.example.cosmocats.common.OrderStatus;
import com.example.cosmocats.domain.Order;
import com.example.cosmocats.dto.order.OrderDto;
import com.example.cosmocats.dto.order.OrderListDto;
import com.example.cosmocats.dto.order.OrderRequestDto;
import com.example.cosmocats.dto.order.UpdateOrderStatusRequestDto;
import com.example.cosmocats.dto.validation.orderNumber.ValidOrderNumber;
import com.example.cosmocats.dto.validation.orderStatus.ValidOrderStatus;
import com.example.cosmocats.service.OrderService;
import com.example.cosmocats.web.mapper.OrderWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;
    private final OrderWebMapper orderWebMapper;

    @GetMapping
    public ResponseEntity<OrderListDto> getAllOrders(
            @RequestParam(name = "customerId", required = false) UUID customerId,
            @RequestParam(name = "status", required = false) OrderStatus orderStatus
    ) {
        List<Order> orders;
        if (customerId != null) {
            orders = orderService.getAllOrdersByCustomerId(customerId);
        } else if (orderStatus != null) {
            orders = orderService.getAllOrdersByStatus(orderStatus);
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

    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderDto> getOrderByOrderNumber(@PathVariable @ValidOrderNumber String orderNumber) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderWebMapper.toOrderDto(
                        orderService.getOrderByNaturalId(orderNumber)));
    }

    @PostMapping
    public ResponseEntity<OrderDto> placeOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderWebMapper.toOrderDto(
                        orderService.placeOrder(orderRequestDto)));
    }

    @PatchMapping("/{orderNumber}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @PathVariable @ValidOrderNumber String orderNumber,
            @RequestBody UpdateOrderStatusRequestDto updateOrderStatusRequestDto
    ) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderWebMapper.toOrderDto(
                        orderService.updateOrderStatus(orderNumber, updateOrderStatusRequestDto)));
    }

    @DeleteMapping("/{orderNumber}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable @ValidOrderNumber String orderNumber) {
        orderService.deleteOrderByNaturalId(orderNumber);
        return ResponseEntity.noContent()
                .build();
    }
}
