package io.github.adrbloch.bookcatalog.bookcatalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private BookRepository bookRepository;
    @Autowired
    public BookController(BookRepository bookRepository) {
    }

    @GetMapping("/all")
    ResponseEntity<List<Book>> viewAll(){
        return ResponseEntity.ok(bookRepository.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Book> viewBook(@PathVariable Long id){
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}
