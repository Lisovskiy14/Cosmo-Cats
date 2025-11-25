package com.example.cosmocats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface NaturalIdRepository<T, ID, NID> extends JpaRepository<T, ID> {
    Optional<T> findByNaturalId(NID naturalId);
    void deleteByNaturalId(NID naturalId);
}
