package com.example.cosmocats.repository.impl;

import com.example.cosmocats.repository.NaturalIdRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;
import java.util.Optional;

public class NaturalIdRepositoryImpl<T, ID, NID extends Serializable> extends SimpleJpaRepository<T, ID> implements NaturalIdRepository<T, ID, NID> {
    private final EntityManager entityManager;

    private NaturalIdRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Optional<T> findByNaturalId(NID naturalId) {
        Session session = entityManager.unwrap(Session.class);
        return session.bySimpleNaturalId(this.getDomainClass())
                .loadOptional(naturalId);
    }

    @Override
    public void deleteByNaturalId(NID naturalId) {
        findByNaturalId(naturalId).ifPresent(this::delete);
    }
}
