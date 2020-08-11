package io.github.adrbloch.bookcatalog.bookcatalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    public BookService(BookRepository bookRepository) {
        Book book = new Book();
        book.setAuthor("jan kowalski");
        book.setTitle("wladca pierscieni");
        book.setDescription("przygody druzyny peirscienia");
        book.setRating(9);
        bookRepository.save(book);
    }
}
