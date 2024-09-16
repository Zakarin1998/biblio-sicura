package com.example.biblioteca_sicura.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.biblioteca_sicura.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}