package io.github.adrbloch.bookcatalog.controller.rest;


import io.github.adrbloch.bookcatalog.domain.Book;
import io.github.adrbloch.bookcatalog.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booksRest")
public class BookRestController {

    private BookService bookService;

    @Autowired
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/all")
    ResponseEntity<List<Book>> viewAll() {
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<Book> viewBook(@PathVariable Long id) {
            return new ResponseEntity<>(bookService.getBookById(id), HttpStatus.OK);

    }

    @PostMapping
    ResponseEntity<Book> createBook(@RequestBody Book newBook) {
        Book book = bookService.createBook(newBook);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<Book> updateBook(@RequestBody Book bookToUpdate, @PathVariable Long id) {
        return new ResponseEntity<>(bookService.updateBook(id, bookToUpdate), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Book> deleteBook(@PathVariable Long id) {
        return new ResponseEntity<>(bookService.deleteBookById(id),HttpStatus.OK);

    }

}
