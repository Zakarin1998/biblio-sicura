package com.example.biblioteca_sicura.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.biblioteca_sicura.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}