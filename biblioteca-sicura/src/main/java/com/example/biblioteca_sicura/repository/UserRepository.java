// src/main/java/com/example/biblioteca/repository/UserRepository.java
package com.example.biblioteca.repository;

import com.example.biblioteca.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
