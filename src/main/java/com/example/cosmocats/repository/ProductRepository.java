package com.example.cosmocats.repository;

import com.example.cosmocats.repository.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    boolean existsByNameAndCategoryId(String name, UUID categoryId);
}
