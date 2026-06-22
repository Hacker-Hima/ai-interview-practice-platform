package com.himachalam.aiinterview.repository;

import com.himachalam.aiinterview.model.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DomainRepository extends JpaRepository<Domain, Long> {
    Optional<Domain> findByName(String name);
    boolean existsByName(String name);
}