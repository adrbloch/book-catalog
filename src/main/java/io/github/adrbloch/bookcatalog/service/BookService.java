package io.github.adrbloch.bookcatalog.service;

import io.github.adrbloch.bookcatalog.domain.Author;
import io.github.adrbloch.bookcatalog.domain.Book;
import io.github.adrbloch.bookcatalog.domain.Publisher;
import io.github.adrbloch.bookcatalog.exception.ResourceAlreadyExistsException;
import io.github.adrbloch.bookcatalog.exception.ResourceNotFoundException;
import io.github.adrbloch.bookcatalog.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    public static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final PublisherService publisherService;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorService authorService, PublisherService publisherService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.publisherService = publisherService;
    }


    public Book getBookById(Long id) {
        logger.info("Get book with id: {}", id);
        return returnBookIfExistsById(id);
    }

    public Book getBookByAuthorNameAndTitle(String authorName, String title) {
        logger.info("Book with author: {" + authorName + "} and title:{" + title + "}");
        return returnBookIfExistsByAuthorNameAndTitle(authorName, title);
    }

    public List<Book> getAllBooks() {
        logger.info("Get all books");
        return bookRepository.findAll();
    }

    public Book createBook(Book book) {
        logger.info("Create book...");

        Book newBook = new Book();
        Author author = book.getAuthor();
        String authorName = author.getName();

        if (bookRepository
                .findByAuthorNameAndTitle(authorName, book.getTitle())
                .isPresent())
            throw new ResourceAlreadyExistsException("Book already exists!");

        Book preparedBook = prepareBookToProcess(book, newBook, authorName);

        return bookRepository.save(preparedBook);
    }

    public Book updateBook(Long id, Book book) {
        logger.info("Update book with id: {}", id);
        returnBookIfExistsById(id);

        Author author = book.getAuthor();
        String authorName = author.getName();

        Book updatedBook = prepareBookToProcess(book, book, authorName);

        Optional<Book> bookByAuthorNameAndTitle = bookRepository
                .findByAuthorNameAndTitle(updatedBook.getAuthor().getName(), book.getTitle());
        if (bookByAuthorNameAndTitle.isPresent() && (!id.equals(bookByAuthorNameAndTitle.get().getId())))
            throw new ResourceAlreadyExistsException("Book already exists!");

        return bookRepository.save(updatedBook);
    }

    public Book deleteBookById(Long id) {
        logger.warn("Delete book with id: {}", id);
        Book bookToDelete = returnBookIfExistsById(id);
        bookRepository.deleteById(id);
        return bookToDelete;
    }

    Book returnBookIfExistsById(Long id) {
        if (bookRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Book with id: {" + id + "} not found!");
        } else return bookRepository.findById(id).get();
    }

    Book returnBookIfExistsByAuthorNameAndTitle(String authorName, String title) {
        if (bookRepository.findByAuthorNameAndTitle(authorName, title).isEmpty()) {
            logger.info("Book with author: {" + authorName + "} and title:{" + title + "}");
            throw new ResourceNotFoundException("Book with author: {" + authorName + "} and title:{" + title + "}) not found!");
        } else
            return bookRepository.findByAuthorNameAndTitle(authorName,title).get();
    }


    Book prepareBookToProcess(Book processingBook, Book bookToSave, String authorName) {

        try {
            Author authorByName = authorService.getAuthorByName(authorName);
            bookToSave.setAuthor(authorByName);

        } catch (ResourceNotFoundException e) {
            Author newAuthor = new Author(authorName);
            bookToSave.setAuthor(newAuthor);
            authorService.createAuthor(newAuthor);
        }

        Publisher publisher = processingBook.getPublisher();
        String publisherName = publisher.getName();
        String publisherCity = publisher.getCity();

        try {
            Publisher publisherByNameAndCity = publisherService.getPublisherByNameAndCity(publisherName, publisherCity);
            bookToSave.setPublisher(publisherByNameAndCity);

        } catch (ResourceNotFoundException e) {
            Publisher newPublisher = new Publisher(publisherName, publisherCity);
            bookToSave.setPublisher(newPublisher);
            publisherService.createPublisher(newPublisher);
        }

        bookToSave.setTitle(processingBook.getTitle());
        bookToSave.setPublicationYear(processingBook.getPublicationYear());
        return bookToSave;
    }


}





