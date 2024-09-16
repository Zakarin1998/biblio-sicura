package com.example.biblioteca_sicura.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.biblioteca_sicura.model.Book;
import com.example.biblioteca_sicura.repository.BookRepository;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> listAll() {
        return bookRepository.findAll();
    }

    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public Optional<Book> getByIsbn(Long isbn) {
        return bookRepository.findById(isbn);
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long isbn, Book bookDetails) {
        Optional<Book> bookOptions = bookRepository.findById(isbn);
        if (bookOptions.isPresent()) {
            Book book = bookOptions.get();
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            return bookRepository.save(book);
        }
        return null;
    }

    public void deleteBook(Long isbn) {
        bookRepository.deleteById(isbn);
    }
}
