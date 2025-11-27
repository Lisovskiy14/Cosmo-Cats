package com.example.cosmocats.repository;

import com.example.cosmocats.repository.entity.CustomerEntity;
import com.example.cosmocats.repository.projection.CustomerDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {
    CustomerEntity findByEmailOrPhoneNumber(String email, String phoneNumber);
    List<CustomerDetailsProjection> findAllBy();
}
