// src/main/java/com/example/biblioteca/repository/BookRepository.java
package com.example.biblioteca_sicura.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.biblioteca_sicura.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
