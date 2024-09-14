// src/main/java/com/example/biblioteca/controller/BookController.java
package com.example.biblioteca_sicura.controller;

import com.example.biblioteca_sicura.model.Book;
import com.example.biblioteca_sicura.model.User;
import com.example.biblioteca_sicura.repository.BookRepository;
import com.example.biblioteca_sicura.repository.UserRepository;
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
