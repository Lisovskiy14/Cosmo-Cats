package com.example.cosmocats.service.impl;

import com.example.cosmocats.common.OrderStatus;
import com.example.cosmocats.domain.Order;
import com.example.cosmocats.domain.OrderItem;
import com.example.cosmocats.domain.Product;
import com.example.cosmocats.dto.order.OrderRequestDto;
import com.example.cosmocats.dto.order.UpdateOrderStatusRequestDto;
import com.example.cosmocats.repository.CustomerRepository;
import com.example.cosmocats.repository.OrderRepository;
import com.example.cosmocats.repository.ProductRepository;
import com.example.cosmocats.repository.entity.OrderEntity;
import com.example.cosmocats.service.OrderService;
import com.example.cosmocats.service.exception.notFound.CustomerNotFoundException;
import com.example.cosmocats.service.exception.notFound.OrderNotFoundException;
import com.example.cosmocats.service.exception.notFound.ProductNotFoundException;
import com.example.cosmocats.service.mapper.CustomerEntityMapper;
import com.example.cosmocats.service.mapper.OrderEntityMapper;
import com.example.cosmocats.service.mapper.ProductEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderEntityMapper orderEntityMapper;
    private final CustomerRepository customerRepository;
    private final CustomerEntityMapper customerEntityMapper;
    private final ProductRepository productRepository;
    private final ProductEntityMapper productEntityMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderEntityMapper::toOrder)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrdersByCustomerId(UUID id) {
        return orderRepository.findAllByCustomerId(id).stream()
                .map(orderEntityMapper::toOrder)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderByNaturalId(String orderNumber) {
        return orderEntityMapper.toOrder(findOrderEntityByNaturalId(orderNumber));
    }

    @Override
    @Transactional
    public Order placeOrder(OrderRequestDto orderRequestDto) {
        UUID customerId = UUID.fromString(orderRequestDto.getCustomerId());
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(orderRequestDto.getCustomerId());
        }

        Set<UUID> productIds = orderRequestDto.getItems().stream()
                .map(orderItem -> UUID.fromString(orderItem.getProductId()))
                .collect(Collectors.toSet());

        Map<UUID, Product> productsMap = productRepository.findAllByIdSet(productIds).stream()
                .map(productEntityMapper::toProduct)
                .collect(Collectors.toMap(Product::getId, product -> product));

        Order order = Order.builder()
                .status(OrderStatus.CREATED)
                .build();

        List<OrderItem> orderItems = orderRequestDto.getItems().stream()
                .map(orderItemRequestDto -> {
                    UUID productId = UUID.fromString(orderItemRequestDto.getProductId());
                    if (!productsMap.containsKey(productId)) {
                        throw new ProductNotFoundException(productId.toString());
                    }

                    return OrderItem.builder()
                            .order(order)
                            .product(productsMap.get(productId))
                            .quantity(orderItemRequestDto.getQuantity())
                            .build();
                })
                .toList();

        order.setItems(orderItems);
        order.calculateTotalPrice();

        OrderEntity orderEntity = orderEntityMapper.toOrderEntity(order);
        orderEntity.setCustomer(customerRepository.getReferenceById(customerId));

        OrderEntity savedOrderEntity = orderRepository.save(orderEntity);
        log.info("New order with id '{}' has been created", savedOrderEntity.getId());
        return orderEntityMapper.toOrder(savedOrderEntity);
    }

    @Override
    @Transactional
    public Order updateOrderStatus(String orderNumber, UpdateOrderStatusRequestDto updateOrderStatusRequestDto) {
        OrderEntity orderEntity = findOrderEntityByNaturalId(orderNumber);

        OrderStatus newStatus = OrderStatus.valueOf(updateOrderStatusRequestDto.getStatus());
        orderEntity.setStatus(newStatus);

        OrderEntity updatedOrderEntity = orderRepository.save(orderEntity);
        log.info("Order with id '{}' has been updated to status '{}'", orderNumber, newStatus);
        return orderEntityMapper.toOrder(updatedOrderEntity);
    }

    @Override
    @Transactional
    public void deleteOrderByNaturalId(String orderNumber) {
        orderRepository.deleteByNaturalId(orderNumber);
        log.info("Order with id '{}' has been deleted", orderNumber);
    }

    private OrderEntity findOrderEntityByNaturalId(String orderNumber) {
        return orderRepository.findByNaturalId(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));
    }
}
