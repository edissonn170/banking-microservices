package com.banking.customer.infrastructure.jpa;

import com.banking.customer.infrastructure.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Repository for PersonEntity.
 */
@Repository
public interface PersonJpaRepository extends JpaRepository<PersonEntity, Long> {

    Optional<PersonEntity> findByIdentification(String identification);

    boolean existsByIdentification(String identification);
}
