package com.example.biblioteca_sicura.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String role; // ROLE_USER o ROLE_ADMIN

    @OneToMany(mappedBy = "user")
    private Set<Book> books;
}
