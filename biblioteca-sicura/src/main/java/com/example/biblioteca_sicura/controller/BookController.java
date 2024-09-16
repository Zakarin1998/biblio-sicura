package com.example.biblioteca_sicura.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.example.biblioteca_sicura.service.BookService;

@RestController
@RequestMapping("/ricerca")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> trova(
        @RequestParam(
            value = "parola", required = false
            ) String parola) {

        if (parola == null || parola.isEmpty()) {
            return bookService.listAll();
        } else {
            return bookService.searchByTitle(parola);
        }
    }

    @PostMapping("/addBook")
    public ResponseEntity<Book> aggiungiLibro(@RequestBody Book book) {
        Book addedBook = bookService.addBook(book);
        return ResponseEntity.ok(addedBook);
    }

    @PutMapping("/aggiorna/{isbn}")
    public ResponseEntity<Book> aggiornaLibro(@PathVariable Long isbn, @RequestBody Book bookDetails) {
        Book updatedBook = bookService.updateBook(isbn, bookDetails);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/cancella/{isbn}")
    public ResponseEntity<Void> deleteLibro(@PathVariable Long isbn) {
        bookService.deleteBook(isbn);
        return ResponseEntity.ok().build();
    }
}

//@RestController
//@RequestMapping("/books")
//public class BookController {

    //@Autowired
    //private BookRepository bookRepository;

    // @GetMapping
    // public List<Book> getAllBooks() {
    //    return bookRepository.findAll();
    // }

    //@PostMapping
    //@Secured("ROLE_ADMIN")
    //public ResponseEntity<?> addBook(@RequestBody Book book) {
    //    bookRepository.save(book);
    //    return ResponseEntity.ok().body("Book added successfully");
    //}

    //@DeleteMapping("/{id}")
    //@Secured("ROLE_ADMIN")
    //public ResponseEntity<?> deleteBook(@PathVariable Long id) {
    //    bookRepository.deleteById(id);
    //    return ResponseEntity.ok().body("Book deleted successfully");
    //}
//}
