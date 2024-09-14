// src/main/java/com/example/biblioteca/repository/BookRepository.java
package com.example.biblioteca.repository;

import com.example.biblioteca.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
