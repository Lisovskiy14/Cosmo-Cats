package com.example.cosmocats.repository;

import com.example.cosmocats.common.OrderStatus;
import com.example.cosmocats.repository.entity.OrderEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends NaturalIdRepository<OrderEntity, UUID, String> {
    List<OrderEntity> findAllByCustomerId(UUID customerId);

    @Query("SELECT o FROM OrderEntity o WHERE o.status = :status ORDER BY o.createdAt ASC")
    List<OrderEntity> findAllByOrderStatus(@Param("status") OrderStatus status);
}
