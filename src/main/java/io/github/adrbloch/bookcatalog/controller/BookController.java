package io.github.adrbloch.bookcatalog.controller;


import io.github.adrbloch.bookcatalog.domain.Book;
import io.github.adrbloch.bookcatalog.repository.BookRepository;
import io.github.adrbloch.bookcatalog.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/all")
    ResponseEntity<List<Book>> viewAll() {
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<Book> viewBook(@PathVariable Long id) {
        return new ResponseEntity<>(bookService.getBook(id), HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<Book> createBook(@RequestBody Book newBook) {
        return new ResponseEntity<>(bookService.createBook(newBook), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    ResponseEntity<Book> updateBook(@RequestBody Book newBook, @PathVariable Long id) {
        return new ResponseEntity<>(bookService.updateBook(newBook, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Book> deleteBook(@PathVariable Long id) {
        return new ResponseEntity<>(bookService.deleteBook(id),HttpStatus.OK);

    }

}
