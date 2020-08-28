package io.github.adrbloch.bookcatalog.controller;


import io.github.adrbloch.bookcatalog.domain.Author;
import io.github.adrbloch.bookcatalog.domain.Book;
import io.github.adrbloch.bookcatalog.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorRestController {

    private AuthorService authorService;

    @Autowired
    public AuthorRestController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/all")
    ResponseEntity<List<Author>> viewAll() {
        return new ResponseEntity<>(authorService.getAllAuthors(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<Author> viewAuthor(@PathVariable Long id) {
        return new ResponseEntity<>(authorService.getAuthor(id), HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<Author> createAuthor(@RequestBody Author newAuthor) {
        return new ResponseEntity<>(authorService.createAuthor(newAuthor), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<Author> updateAuthor(@RequestBody Author newAuthor, @PathVariable Long id) {
        return new ResponseEntity<>(authorService.updateAuthor(newAuthor, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Author> deleteAuthor(@PathVariable Long id) {
        return new ResponseEntity<>(authorService.deleteAuthor(id),HttpStatus.OK);

    }

}
