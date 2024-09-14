// src/main/java/com/example/biblioteca/model/Book.java
package com.example.biblioteca_sicura.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // chi ha aggiunto il libro
}