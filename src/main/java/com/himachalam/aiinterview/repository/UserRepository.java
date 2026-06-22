package com.himachalam.aiinterview.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.himachalam.aiinterview.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}