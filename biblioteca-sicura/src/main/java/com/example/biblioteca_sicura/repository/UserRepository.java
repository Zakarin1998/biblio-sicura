package com.example.biblioteca_sicura.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.biblioteca_sicura.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // User findByUsername(String username);
	Optional<User> findByUsernameOrEmail(String username, String email);
}
