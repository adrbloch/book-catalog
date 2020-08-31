package io.github.adrbloch.bookcatalog.service;

import io.github.adrbloch.bookcatalog.domain.Book;
import io.github.adrbloch.bookcatalog.exception.ResourceAlreadyExistsException;
import io.github.adrbloch.bookcatalog.exception.ResourceNotFoundException;
import io.github.adrbloch.bookcatalog.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    public static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;

    }

    public Book getBook(Long id) throws ResourceNotFoundException {
        logger.info("Get book with id: {}", id);
        return checkIfExistsAndReturnBook(id);
    }

    public List<Book> getAllBooks() {
        logger.info("Get all books");
        return bookRepository.findAll();
    }

    public Book createBook(Book book) throws ResourceAlreadyExistsException{
        logger.info("Create book...");
        if (bookRepository.existsByAuthorNameAndTitle(book.getAuthor().getName(), book.getTitle()))
            throw new ResourceAlreadyExistsException("Book already exists!");
        else
            return bookRepository.save(book);

    }

    public Book updateBook(Book book, Long id) throws ResourceNotFoundException {
        logger.info("Update book with id: {}", id);

        Book bookToUpdate = checkIfExistsAndReturnBook(id);
        bookToUpdate.setTitle(book.getTitle());
        bookToUpdate.setAuthor(book.getAuthor());
        bookToUpdate.setPublisher(book.getPublisher());
        bookToUpdate.setPublicationYear(book.getPublicationYear());

        return bookRepository.save(bookToUpdate);
    }


    public Book deleteBook(Long id) throws ResourceNotFoundException {
        logger.warn("Delete book with id: {}", id);
        Book bookToDelete = checkIfExistsAndReturnBook(id);
        bookRepository.deleteById(id);
        return bookToDelete;
    }

    private Book checkIfExistsAndReturnBook(Long id) {
        if (bookRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Book with id {" + id + "} not found!");
        } else return bookRepository.findById(id).get();
    }


}





