package com.example.biblioteca_sicura.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.biblioteca_sicura.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
