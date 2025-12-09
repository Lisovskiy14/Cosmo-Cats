package com.example.cosmocats.repository;

import com.example.cosmocats.repository.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    boolean existsByNameAndCategoryId(String name, UUID categoryId);

    @Query("SELECT p FROM ProductEntity p WHERE p.id IN (:ids)")
    List<ProductEntity> findAllByIdSet(@Param("ids") Set<UUID> ids);
}
