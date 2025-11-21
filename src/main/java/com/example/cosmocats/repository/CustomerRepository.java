package com.example.cosmocats.repository;

import com.example.cosmocats.repository.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {
    CustomerEntity findByEmailOrPhoneNumber(String email, String phoneNumber);
}
