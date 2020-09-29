package io.github.adrbloch.bookcatalog.service;

import io.github.adrbloch.bookcatalog.domain.Author;
import io.github.adrbloch.bookcatalog.domain.Book;
import io.github.adrbloch.bookcatalog.domain.Publisher;
import io.github.adrbloch.bookcatalog.exception.ResourceAlreadyExistsException;
import io.github.adrbloch.bookcatalog.exception.ResourceNotFoundException;
import io.github.adrbloch.bookcatalog.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BookRepository bookRepository;
    @Mock
    AuthorService authorService;
    @Mock
    PublisherService publisherService;
    @InjectMocks
    BookService bookService;


    private Book initializeBook() {

        Author author = new Author("Andrzej Sapkowski");
        Publisher publisher = new Publisher("SuperNowa", "Warsaw");
        Book book = new Book(author, "The Witcher", publisher, 1990);
        book.setId(1L);

        return book;
    }

    @Test
    void createAlreadyExistingBookThrowsException() {

        //given
        Book book = initializeBook();
        given(bookRepository.findByAuthorNameAndTitle(book.getAuthor().getName(), book.getTitle()))
                .willReturn(Optional.of(book));

        //when
        //then
        assertThrows(ResourceAlreadyExistsException.class, () -> bookService.createBook(book));
    }

    @Test
    void returnBookAfterCreate() {

        //given
        Book book = initializeBook();
        Author author = book.getAuthor();
        String authorName = author.getName();
        Publisher publisher = book.getPublisher();
        given(authorService.getAuthorByName(authorName)).willReturn(author);
        given(publisherService.getPublisherByNameAndCity(publisher.getName(), publisher.getCity()))
                .willReturn(publisher);
        given(bookRepository.save(any(Book.class))).willReturn(book);

        //when
        Book createdBook = bookService.createBook(book);

        //then
        assertEquals(1L, createdBook.getId());
        assertEquals("Andrzej Sapkowski", createdBook.getAuthor().getName());
        assertEquals("SuperNowa", createdBook.getPublisher().getName());
        assertEquals("Warsaw", createdBook.getPublisher().getCity());
        assertEquals("The Witcher", createdBook.getTitle());
        assertEquals(1990, createdBook.getPublicationYear());
    }

    @Test
    void ifTryToUpdateToAlreadyExistingBookWithDifferentIdThrowException() {

        //given
        Book book = initializeBook();
        Book book2 = initializeBook();
        book2.setId(2L);
        Author author = book.getAuthor();
        Publisher publisher = book.getPublisher();
        given(bookRepository
                .findByAuthorNameAndTitle(author.getName(), book.getTitle())).willReturn(Optional.of(book2));
        given(bookRepository.findById(book.getId())).willReturn(Optional.of(book));
        given(authorService.getAuthorByName(author.getName())).willReturn(author);
        given(publisherService.getPublisherByNameAndCity(publisher.getName(), publisher.getCity()))
                .willReturn(publisher);

        //when
        //then
        assertThrows(ResourceAlreadyExistsException.class, () -> bookService.updateBook(1L, book));
    }

    @Test
    void returnBookAfterUpdate() {

        //given
        Book book = initializeBook();
        Author author = book.getAuthor();
        Publisher publisher = book.getPublisher();
        given(authorService.getAuthorByName(author.getName())).willReturn(author);
        given(publisherService.getPublisherByNameAndCity(publisher.getName(), publisher.getCity()))
                .willReturn(publisher);
        given(bookRepository
                .findByAuthorNameAndTitle(author.getName(), book.getTitle())).willReturn(Optional.of(book));
        given(bookRepository.findById(book.getId())).willReturn(Optional.of(book));
        given(bookRepository.save(book)).willReturn(book);


        //when
        Book updatedBook = bookService.updateBook(1L, book);

        //then
        assertEquals(1L, updatedBook.getId());
        assertEquals("Andrzej Sapkowski", updatedBook.getAuthor().getName());
        assertEquals("SuperNowa", updatedBook.getPublisher().getName());
        assertEquals("Warsaw", updatedBook.getPublisher().getCity());
        assertEquals("The Witcher", updatedBook.getTitle());
        assertEquals(1990, updatedBook.getPublicationYear());
    }

    @Test
    void returnBookAfterDeleteById() {

        //given
        Book book = initializeBook();
        Long id = book.getId();
        given(bookRepository.findById(id)).willReturn(Optional.of(book));

        //when
        Book deletedBook = bookService.deleteBookById(id);

        //then
        assertEquals(1L, deletedBook.getId());
        assertEquals("Andrzej Sapkowski", deletedBook.getAuthor().getName());
        assertEquals("SuperNowa", deletedBook.getPublisher().getName());
        assertEquals("Warsaw", deletedBook.getPublisher().getCity());
        assertEquals("The Witcher", deletedBook.getTitle());
        assertEquals(1990, deletedBook.getPublicationYear());
    }


    @Test
    void ifBookNotExistsByIdThrowException() {

        //given
        Book book = new Book();
        Long id = book.getId();
        given(bookRepository.findById(id)).willReturn(Optional.empty());

        //when
        //then
        assertThrows(ResourceNotFoundException.class, () -> bookService.returnBookIfExistsById(id));
    }

    @Test
    void returnBookIfExistsById() {

        //given
        Book book = initializeBook();
        Long id = book.getId();
        given(bookRepository.findById(id)).willReturn(Optional.of(book));

        //when
        Book checkedBook = bookService.returnBookIfExistsById(id);

        //then
        assertEquals(1L, checkedBook.getId());
        assertEquals("Andrzej Sapkowski", checkedBook.getAuthor().getName());
        assertEquals("SuperNowa", checkedBook.getPublisher().getName());
        assertEquals("Warsaw", checkedBook.getPublisher().getCity());
        assertEquals("The Witcher", checkedBook.getTitle());
        assertEquals(1990, checkedBook.getPublicationYear());
    }

    @Test
    void ifnBookNotExistsByAuthorNameAndTitleThrowException() {

        //given
        Book book = initializeBook();
        String authorName = book.getAuthor().getName();
        String bookTitle = book.getTitle();
        given(bookRepository.findByAuthorNameAndTitle(authorName, bookTitle)).willReturn(Optional.empty());

        //when
        //then
        assertThrows(ResourceNotFoundException.class, () -> bookService
                .returnBookIfExistsByAuthorNameAndTitle(authorName, bookTitle));
    }

    @Test
    void returnPublisherIfExistsByNameAndCity() {

        //given
        Book book = initializeBook();
        String authorName = book.getAuthor().getName();
        String bookTitle = book.getTitle();
        given(bookRepository.findByAuthorNameAndTitle(authorName, bookTitle)).willReturn(Optional.of(book));

        //when
        Book checkedBook = bookService.returnBookIfExistsByAuthorNameAndTitle(authorName, bookTitle);

        //then
        assertEquals("Andrzej Sapkowski", checkedBook.getAuthor().getName());
        assertEquals("SuperNowa", checkedBook.getPublisher().getName());
        assertEquals("Warsaw", checkedBook.getPublisher().getCity());
        assertEquals("The Witcher", checkedBook.getTitle());
        assertEquals(1990, checkedBook.getPublicationYear());
    }


    @Test
    void prepareBookToProcessMethodReturnsThatBookWhenAuthorNotExistsAndPublisherExists() {

        //given
        Book book = initializeBook();
        String authorName = book.getAuthor().getName();
        Publisher publisher = book.getPublisher();
        given(authorService.getAuthorByName(authorName)).willThrow(ResourceNotFoundException.class);
        given(publisherService.getPublisherByNameAndCity(publisher.getName(), publisher.getCity()))
                .willReturn(publisher);

        //when
        Book preparedBook = bookService.prepareBookToProcess(book, book, authorName);

        //then
        assertEquals("Andrzej Sapkowski", preparedBook.getAuthor().getName());
        assertEquals("SuperNowa", preparedBook.getPublisher().getName());
        assertEquals("Warsaw", preparedBook.getPublisher().getCity());
        assertEquals("The Witcher", preparedBook.getTitle());
        assertEquals(1990, preparedBook.getPublicationYear());
    }

    @Test
    void prepareBookToProcessMethodReturnsThatBookWhenAuthorAndPublisherExist() {

        //given
        Book book = initializeBook();
        Author author = book.getAuthor();
        String authorName = author.getName();
        Publisher publisher = book.getPublisher();
        given(authorService.getAuthorByName(authorName)).willReturn(author);
        given(publisherService.getPublisherByNameAndCity(publisher.getName(), publisher.getCity()))
                .willReturn(publisher);

        //when
        Book preparedBook = bookService.prepareBookToProcess(book, book, authorName);

        //then
        assertEquals("Andrzej Sapkowski", preparedBook.getAuthor().getName());
        assertEquals("SuperNowa", preparedBook.getPublisher().getName());
        assertEquals("Warsaw", preparedBook.getPublisher().getCity());
        assertEquals("The Witcher", preparedBook.getTitle());
        assertEquals(1990, preparedBook.getPublicationYear());
    }

    @Test
    void prepareBookToProcessMethodReturnsThatBookWhenAuthorAndPublisherNotExist() {

        //given
        Book book = initializeBook();
        Author author = book.getAuthor();
        String authorName = author.getName();
        Publisher publisher = book.getPublisher();
        given(authorService.getAuthorByName(authorName)).willThrow(ResourceNotFoundException.class);
        given(publisherService.getPublisherByNameAndCity(publisher.getName(), publisher.getCity()))
                .willThrow(ResourceNotFoundException.class);

        //when
        Book preparedBook = bookService.prepareBookToProcess(book, book, authorName);

        //then
        assertEquals("Andrzej Sapkowski", preparedBook.getAuthor().getName());
        assertEquals("SuperNowa", preparedBook.getPublisher().getName());
        assertEquals("Warsaw", preparedBook.getPublisher().getCity());
        assertEquals("The Witcher", preparedBook.getTitle());
        assertEquals(1990, preparedBook.getPublicationYear());
    }

    @Test
    void prepareBookToProcessMethodReturnsThatBookWhenAuthorNotExistAndPublisherExist() {

        //given
        Book book = initializeBook();
        Author author = book.getAuthor();
        String authorName = author.getName();
        Publisher publisher = book.getPublisher();
        given(authorService.getAuthorByName(authorName)).willThrow(ResourceNotFoundException.class);
        given(publisherService.getPublisherByNameAndCity(publisher.getName(), publisher.getCity()))
                .willReturn(publisher);

        //when
        Book preparedBook = bookService.prepareBookToProcess(book, book, authorName);

        //then
        assertEquals("Andrzej Sapkowski", preparedBook.getAuthor().getName());
        assertEquals("SuperNowa", preparedBook.getPublisher().getName());
        assertEquals("Warsaw", preparedBook.getPublisher().getCity());
        assertEquals("The Witcher", preparedBook.getTitle());
        assertEquals(1990, preparedBook.getPublicationYear());
    }

}