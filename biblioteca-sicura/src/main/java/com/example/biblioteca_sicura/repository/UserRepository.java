package com.example.biblioteca_sicura.repository;

import com.example.biblioteca_sicura.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String email);
}
