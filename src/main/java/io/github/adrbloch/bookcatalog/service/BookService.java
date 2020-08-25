package io.github.adrbloch.bookcatalog.service;

import io.github.adrbloch.bookcatalog.controller.BookController;
import io.github.adrbloch.bookcatalog.domain.Book;
import io.github.adrbloch.bookcatalog.exception.ResourceNotFoundException;
import io.github.adrbloch.bookcatalog.repository.AuthorRepository;
import io.github.adrbloch.bookcatalog.repository.BookRepository;
import io.github.adrbloch.bookcatalog.repository.PublisherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    public static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private PublisherRepository publisherRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }

    public Book getBook(Long id) throws ResourceNotFoundException {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id {" + "} not found!"));
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

//    public Book createBook(Book book){
//
//    }
}

