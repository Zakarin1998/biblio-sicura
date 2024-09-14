Certamente! Ti guiderò attraverso la creazione di un progetto di biblioteca usando Spring Boot. Questo progetto includerà:

1. **Creazione del database per utenti e libri**
2. **Gestione degli utenti e autenticazione**
3. **Gestione dei libri, con possibilità di aggiungere libri solo se l'utente è un admin**

### 1. Configurazione del Progetto

#### a. Creazione del Progetto

1. Vai su [Spring Initializr](https://start.spring.io/).
2. Seleziona le seguenti opzioni:
   - **Project:** Maven Project
   - **Language:** Java
   - **Spring Boot Version:** (scegli l'ultima versione stabile)
   - **Project Metadata:**
     - **Group:** com.example
     - **Artifact:** biblioteca
     - **Name:** biblioteca
     - **Description:** Biblioteca management system
     - **Package name:** com.example.biblioteca
     - **Packaging:** Jar
     - **Java:** 11 o superiore
   - **Dependencies:**
     - Spring Web
     - Spring Data JPA
     - H2 Database (per il database in memoria)
     - Spring Security
     - Lombok (opzionale, ma utile per ridurre il boilerplate code)

3. Clicca su "Generate" per scaricare il progetto e poi estrai il file zip.

#### b. Importazione in IDE

1. Importa il progetto nel tuo IDE (Eclipse, IntelliJ IDEA, etc.) come progetto Maven.

### 2. Configurazione del Database

#### a. Creazione delle Entità

Crea due entità, `User` e `Book`, con le rispettive proprietà.

```java
// src/main/java/com/example/biblioteca/model/User.java
package com.example.biblioteca.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String role; // ROLE_USER o ROLE_ADMIN

    @OneToMany(mappedBy = "user")
    private Set<Book> books;
}
```

```java
// src/main/java/com/example/biblioteca/model/Book.java
package com.example.biblioteca.model;

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
```

#### b. Creazione dei Repository

Crea i repository per le entità `User` e `Book`.

```java
// src/main/java/com/example/biblioteca/repository/UserRepository.java
package com.example.biblioteca.repository;

import com.example.biblioteca.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
```

```java
// src/main/java/com/example/biblioteca/repository/BookRepository.java
package com.example.biblioteca.repository;

import com.example.biblioteca.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
```

### 3. Configurazione di Spring Security

#### a. Configurazione di Sicurezza

Configura Spring Security per gestire l'autenticazione e l'autorizzazione.

```java
// src/main/java/com/example/biblioteca/security/SecurityConfig.java
package com.example.biblioteca.security;

import com.example.biblioteca.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
            .and()
            .logout()
                .permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

#### b. Servizio per Utenti

Crea un servizio per gestire i dettagli dell'utente.

```java
// src/main/java/com/example/biblioteca/service/UserDetailsServiceImpl.java
package com.example.biblioteca.service;

import com.example.biblioteca.model.User;
import com.example.biblioteca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRole())
            .build();
    }
}
```

### 4. Implementazione dei Controller

#### a. Controller per Utenti e Autenticazione

```java
// src/main/java/com/example/biblioteca/controller/AuthController.java
package com.example.biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
```

#### b. Controller per Gestione Libri

```java
// src/main/java/com/example/biblioteca/controller/BookController.java
package com.example.biblioteca.controller;

import com.example.biblioteca.model.Book;
import com.example.biblioteca.model.User;
import com.example.biblioteca.repository.BookRepository;
import com.example.biblioteca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "books/list";
    }

    @GetMapping("/add")
    public String addBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "books/add";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        book.setUser(user);
        bookRepository.save(book);
        return "redirect:/books";
    }
}
```

### 5. Configurazione delle Viste

Crea le viste per login, gestione libri, e altre pagine necessarie.

#### a. Pagina di Login

```html
<!-- src/main/resources/templates/login.html -->
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <form action="/login" method="post">
        <div>
            <label>Username:</label>
            <input type="text" name="username"/>
        </div>
        <div>
            <label>Password:</label>
            <input type="password" name="password"/>
        </div>
        <button type="submit">Login</button>
    </form>
</body>
</html>
```

#### b. Pagina di Lista Libri

```html
<!-- src/main/resources/templates/books/list.html -->
<!DOCTYPE html>
<html>
<head>
    <title>Books</title>
</head>
<body>
    <h1>Books</h1>
    <a href="/books/add">Add New Book</a>
    <ul>
        <th:block th:each="book : ${books}">
            <li th:text="${book.title} + ' by ' + ${book.author}"></li>
        </th:block>
    </ul>
</body>
</html>
```

#### c. Pagina di Aggiunta Libro

```html
<!-- src/main/resources/templates/books/add.html -->
<!DOCTYPE html>
<html>
<head>
    <title>Add Book</title>
</head>
<body>
    <h1>Add Book</h1>
    <form action="/books/add" method="post">
        <div>
            <label>Title:</label>
            <input type="text" name="title"/>
        </div>
        <div>
            <label>Author:</label>
            <input type

="text" name="author"/>
        </div>
        <button type="submit">Add Book</button>
    </form>
</body>
</html>
```

### Conclusioni

Hai ora una configurazione di base per un'applicazione di gestione di una biblioteca con Spring Boot. Puoi estendere questo progetto aggiungendo funzionalità come la validazione dei dati, gestione degli errori, e altre caratteristiche a seconda delle tue esigenze.