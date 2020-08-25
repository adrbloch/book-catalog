package io.github.adrbloch.bookcatalog.controller;


import io.github.adrbloch.bookcatalog.domain.Book;
import io.github.adrbloch.bookcatalog.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    public static final Logger logger = LoggerFactory.getLogger(BookController.class);
    private BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/all")
    ResponseEntity<List<Book>> viewAll() {
        logger.info("View all the books");
        return ResponseEntity.ok(bookRepository.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Book> viewBook(@PathVariable Long id) {
        logger.info("View book with id: {}", id);
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<Book> createBook(@RequestBody Book newBook) {
        logger.info("Create new book");
        Book result = bookRepository.save(newBook);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

//    @Transactional
//    @PutMapping("/{id}")
//    ResponseEntity<Book> updateBook(@RequestBody Book newBook, @PathVariable Long id) {
//        logger.info("Update book with id: {}", id);
//
//        if (!bookRepository.existsById(id)) {
//            logger.warn("Book not found with id: {}", id);
//            return ResponseEntity.notFound().build();
//        }
//        bookRepository.findById(id)
//                .ifPresent(book -> {
//                    book.setAuthor(newBook.getAuthor());
//                    book.setTitle(newBook.getTitle());
//                    book.setDescription(newBook.getDescription());
//                    book.setRating(newBook.getRating());
//                    bookRepository.save(book);
//                });
//        return ResponseEntity.noContent().build();
//    }

    @DeleteMapping("/{id}")
    ResponseEntity<Book> deleteBook(@PathVariable Long id) {
        logger.info("Delete book with id: {}", id);
        if (!bookRepository.existsById(id)) {
            logger.warn("Book not found with id: {}", id);
            return ResponseEntity.notFound().build();
        }
        bookRepository.deleteById(id);
        return ResponseEntity.ok().build();

    }


    @GetMapping("/index")
    public String getIndex(Model model){
        return "index";
    }
}
