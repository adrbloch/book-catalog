package io.github.adrbloch.bookcatalog.service;

import io.github.adrbloch.bookcatalog.domain.Author;
import io.github.adrbloch.bookcatalog.domain.Book;
import io.github.adrbloch.bookcatalog.domain.Publisher;
import io.github.adrbloch.bookcatalog.exception.ResourceAlreadyExistsException;
import io.github.adrbloch.bookcatalog.exception.ResourceNotFoundException;
import io.github.adrbloch.bookcatalog.repository.AuthorRepository;
import io.github.adrbloch.bookcatalog.repository.BookRepository;
import io.github.adrbloch.bookcatalog.repository.PublisherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    public static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private PublisherRepository publisherRepository;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }


    public Book getBookById(Long id) {
        logger.info("Get book with id: {}", id);
        return checkIfExistsAndReturnBook(id);
    }

    public List<Book> getAllBooks() {
        logger.info("Get all books");
        return bookRepository.findAll();
    }

    public Book createBook(Book book) {
        logger.info("Create book...");
        Book newBook = prepareToSave(book);

        return bookRepository.save(newBook);
    }

    public Book updateBook(Long id, Book book) throws ResourceAlreadyExistsException {
        logger.info("Update book with id: {}", id);
        checkIfExistsAndReturnBook(id);
        Book updatedBook = prepareToUpdate(book);

        Optional<Book> bookByAuthorNameAndTitle = bookRepository.findByAuthorNameAndTitle(updatedBook.getAuthor().getName(), book.getTitle());
        if (bookByAuthorNameAndTitle.isPresent() && (!id.equals(bookByAuthorNameAndTitle.get().getId())))
            throw new ResourceAlreadyExistsException("Book already exists!");

        return bookRepository.save(updatedBook);
    }

    public Book deleteBookById(Long id) {
        logger.warn("Delete book with id: {}", id);
        Book bookToDelete = checkIfExistsAndReturnBook(id);
        bookRepository.deleteById(id);
        return bookToDelete;
    }

    private Book checkIfExistsAndReturnBook(Long id) throws ResourceNotFoundException {
        if (bookRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Book with id {" + id + "} not found!");
        } else return bookRepository.findById(id).get();
    }

    private Book prepareToSave(Book book) throws ResourceAlreadyExistsException {

        Book newBook = new Book();
        Author author = book.getAuthor();
        String authorName = author.getName();

        if (bookRepository
                .findByAuthorNameAndTitle(authorName, book.getTitle())
                .isPresent())
            throw new ResourceAlreadyExistsException("Book already exists!");

        Optional<Author> authorByName = authorRepository.findByName(authorName);
        if (authorByName.isPresent()) {
            newBook.setAuthor(authorByName.get());

        } else {
            Author newAuthor = new Author(authorName);
            newBook.setAuthor(newAuthor);
            authorRepository.save(newAuthor);
        }


        Publisher publisher = book.getPublisher();
        String publisherName = publisher.getName();
        String publisherCity = publisher.getCity();

        Optional<Publisher> publisherByNameAndCity = publisherRepository.findByNameAndCity(publisherName, publisherCity);
        if (publisherByNameAndCity.isPresent()) {
            newBook.setPublisher(publisherByNameAndCity.get());

        } else {
            Publisher newPublisher = new Publisher(publisherName, publisherCity);
            newBook.setPublisher(newPublisher);
            publisherRepository.save(newPublisher);
        }

        newBook.setTitle(book.getTitle());
        newBook.setPublicationYear(book.getPublicationYear());
        return newBook;
    }

    private Book prepareToUpdate(Book book) {

        Author author = book.getAuthor();
        String authorName = author.getName();


            Optional<Author> authorByName = authorRepository.findByName(authorName);
            if (authorByName.isPresent()) {
                book.setAuthor(authorByName.get());
        } else {
            Author newAuthor = new Author(authorName);
            book.setAuthor(newAuthor);
            authorRepository.save(newAuthor);
        }


        Publisher publisher = book.getPublisher();
        String publisherName = publisher.getName();
        String publisherCity = publisher.getCity();

        Optional<Publisher> publisherByNameAndCity = publisherRepository.findByNameAndCity(publisherName, publisherCity);
        if (publisherByNameAndCity.isPresent()) {
            book.setPublisher(publisherByNameAndCity.get());

        } else {
            Publisher newPublisher = new Publisher(publisherName, publisherCity);
            book.setPublisher(newPublisher);
            publisherRepository.save(newPublisher);
        }

        book.setTitle(book.getTitle());
        book.setPublicationYear(book.getPublicationYear());

        return book;
    }

}





