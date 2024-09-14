package com.example.biblioteca_sicura.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.biblioteca_sicura.model.Book;
import com.example.biblioteca_sicura.repository.BookRepository;

@RestController
@RequestMapping("/ricerca")
public class SearchController {

    @Autowired
    private BookRepository bookRepository;

    // Endpoint per la ricerca dei libri
    @GetMapping
    public ResponseEntity<?> trova(@RequestParam(value = "parola", required = false) String parola) {
        List<Book> books;
        if (parola == null || parola.isEmpty()) {
            books = bookRepository.findAll();
        } else {
            books = bookRepository.findByTitleContainingIgnoreCase(parola);
        }
        return ResponseEntity.ok(books);
    }

    // Endpoint per aggiungere un libro (solo ADMIN)
    @PostMapping("/addBook")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> aggiungiLibro(@RequestBody Book libro) {
        bookRepository.save(libro);
        return ResponseEntity.ok("Libro aggiunto con successo");
    }

    // Endpoint per aggiornare un libro (solo ADMIN)
    @PutMapping("/aggiorna/{isbn}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> aggiornaLibro(@PathVariable Long isbn, @RequestBody Book libro) {
        return bookRepository.findById(isbn).map(existingBook -> {
            existingBook.setTitle(libro.getTitle());
            existingBook.setAuthor(libro.getAuthor());
            bookRepository.save(existingBook);
            return ResponseEntity.ok("Libro aggiornato con successo");
        }).orElse(ResponseEntity.notFound().build());
    }

    // Endpoint per cancellare un libro (solo ADMIN)
    @DeleteMapping("/cancella/{isbn}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteLibro(@PathVariable Long isbn) {
        return bookRepository.findById(isbn).map(book -> {
            bookRepository.delete(book);
            return ResponseEntity.ok("Libro cancellato con successo");
        }).orElse(ResponseEntity.notFound().build());
    }
}
