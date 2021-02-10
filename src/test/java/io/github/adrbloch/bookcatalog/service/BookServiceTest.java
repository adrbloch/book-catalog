package io.github.adrbloch.bookcatalog.service;

import io.github.adrbloch.bookcatalog.model.Author;
import io.github.adrbloch.bookcatalog.model.Book;
import io.github.adrbloch.bookcatalog.model.Publisher;
import io.github.adrbloch.bookcatalog.exception.ResourceAlreadyExistsException;
import io.github.adrbloch.bookcatalog.exception.ResourceNotFoundException;
import io.github.adrbloch.bookcatalog.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

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

    MultipartFile bookImgMultipartFile;
    Book book;


    @BeforeEach
    void initializeBook() throws IOException {

        var bookImgFile = new File("src/test/resources/static/img/bookCover/witcher.jpg");
        bookImgMultipartFile = new MockMultipartFile("witcher.jpg", new FileInputStream(bookImgFile));

        var author = new Author("Andrzej Sapkowski");
        var publisher = new Publisher("SuperNowa", "Warsaw");
        book = new Book(author, "The Witcher", publisher, 1990);
        book.setId(1L);

        book.setImage(Base64
                .getEncoder()
                .encodeToString(bookImgMultipartFile.getBytes()));
    }

    @Test
    void createAlreadyExistingBookThrowsException() {

        //given
        given(bookRepository.findByAuthorNameAndTitle(book.getAuthor().getName(), book.getTitle()))
                .willReturn(Optional.of(book));

        //when
        //then
        assertThrows(ResourceAlreadyExistsException.class, () -> bookService.createBook(book, bookImgMultipartFile));
    }

    @Test
    void returnBookAfterCreate() {

        //given
        Author author = book.getAuthor();
        String authorName = author.getName();
        Publisher publisher = book.getPublisher();

        given(authorService.getAuthorByName(authorName)).willReturn(author);
        given(publisherService.getPublisherByNameAndCity(publisher.getName(), publisher.getCity()))
                .willReturn(publisher);
        given(bookRepository.save(any(Book.class))).willReturn(book);

        //when
        Book createdBook = bookService.createBook(book, bookImgMultipartFile);

        //then
        assertEquals(1L, createdBook.getId());
        assertEquals("Andrzej Sapkowski", createdBook.getAuthor().getName());
        assertEquals("SuperNowa", createdBook.getPublisher().getName());
        assertEquals("Warsaw", createdBook.getPublisher().getCity());
        assertEquals("The Witcher", createdBook.getTitle());
        assertEquals(1990, createdBook.getPublicationYear());
        assertNotNull(createdBook.getImage());
    }

    @Test
    void ifTryToUpdateToAlreadyExistingBookThrowException() {

        //given
        Author author = book.getAuthor();
        String title = book.getTitle();
        Publisher publisher = book.getPublisher();
        int publicationYear = book.getPublicationYear();

        var newBook = new Book(author, title, publisher, publicationYear);
        newBook.setId(2L);

        String authorName = newBook.getAuthor().getName();
        String publisherName = newBook.getPublisher().getName();
        String publisherCity = newBook.getPublisher().getCity();

        given(bookRepository.findById(newBook.getId())).willReturn(Optional.of(newBook));
        given(authorService.getAuthorByName(authorName)).willReturn(author);
        given(publisherService.getPublisherByNameAndCity(publisherName, publisherCity))
                .willReturn(publisher);
        given(bookRepository
                .findByAuthorNameAndTitle(authorName, newBook.getTitle()))
                .willReturn(Optional.of(book));

        //when
        //then
        assertThrows(ResourceAlreadyExistsException.class, () ->
                bookService.updateBook(2L, newBook, bookImgMultipartFile));
    }

    @Test
    void updateBookWithNoChangesReturnsSameBook() {

        //given
        Author author = book.getAuthor();
        Publisher publisher = book.getPublisher();

        given(authorService.getAuthorByName(author.getName())).willReturn(author);
        given(publisherService.getPublisherByNameAndCity(publisher.getName(), publisher.getCity()))
                .willReturn(publisher);
        given(bookRepository
                .findByAuthorNameAndTitle(author.getName(), book.getTitle()))
                .willReturn(Optional.of(book));
        given(bookRepository.findById(book.getId())).willReturn(Optional.of(book));
        given(bookRepository.save(book)).willReturn(book);


        //when
        Book updatedBook = bookService.updateBook(1L, book, bookImgMultipartFile);

        //then
        assertEquals(1L, updatedBook.getId());
        assertEquals("Andrzej Sapkowski", updatedBook.getAuthor().getName());
        assertEquals("SuperNowa", updatedBook.getPublisher().getName());
        assertEquals("Warsaw", updatedBook.getPublisher().getCity());
        assertEquals("The Witcher", updatedBook.getTitle());
        assertEquals(1990, updatedBook.getPublicationYear());
        assertNotNull(updatedBook.getImage());
    }

    @Test
    void returnBookAfterUpdate() {

        //given
        Book newBook = book;
        newBook.setAuthor(new Author("Dmitrij Gluchowski"));
        newBook.setTitle("Metro 2033");
        newBook.setPublisher(new Publisher("Insignis Media", "Cracow"));
        newBook.setPublicationYear(2010);

        Author author = newBook.getAuthor();
        Publisher publisher = newBook.getPublisher();

        given(authorService.getAuthorByName(author.getName())).willReturn(author);
        given(publisherService.getPublisherByNameAndCity(publisher.getName(), publisher.getCity()))
                .willReturn(publisher);
        given(bookRepository
                .findByAuthorNameAndTitle(author.getName(), newBook.getTitle()))
                .willReturn(Optional.of(book));
        given(bookRepository.findById(book.getId())).willReturn(Optional.of(book));
        given(bookRepository.save(newBook)).willReturn(newBook);


        //when
        Book updatedBook = bookService.updateBook(1L, newBook, bookImgMultipartFile);

        //then
        assertEquals(1L, updatedBook.getId());
        assertEquals("Dmitrij Gluchowski", updatedBook.getAuthor().getName());
        assertEquals("Insignis Media", updatedBook.getPublisher().getName());
        assertEquals("Cracow", updatedBook.getPublisher().getCity());
        assertEquals("Metro 2033", updatedBook.getTitle());
        assertEquals(2010, updatedBook.getPublicationYear());
        assertNotNull(updatedBook.getImage());
    }

    @Test
    void returnBookAfterDeleteById() {

        //given
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
        assertNotNull(deletedBook.getImage());
    }


    @Test
    void ifBookNotExistsByIdThrowException() {

        //given
        Long id = book.getId();
        given(bookRepository.findById(id)).willReturn(Optional.empty());

        //when
        //then
        assertThrows(ResourceNotFoundException.class, () -> bookService.returnBookIfExistsById(id));
    }

    @Test
    void returnBookIfExistsById() {

        //given
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
        assertNotNull(checkedBook.getImage());
    }

    @Test
    void ifBookNotExistsByAuthorNameAndTitleThrowException() {

        //given
        String authorName = book.getAuthor().getName();
        String bookTitle = book.getTitle();
        given(bookRepository.findByAuthorNameAndTitle(authorName, bookTitle))
                .willReturn(Optional.empty());

        //when
        //then
        assertThrows(ResourceNotFoundException.class, () -> bookService
                .returnBookIfExistsByAuthorNameAndTitle(authorName, bookTitle));
    }

    @Test
    void returnPublisherIfExistsByNameAndCity() {

        //given
        String authorName = book.getAuthor().getName();
        String bookTitle = book.getTitle();
        given(bookRepository.findByAuthorNameAndTitle(authorName, bookTitle)).willReturn(Optional.of(book));

        //when
        Book checkedBook = bookService
                .returnBookIfExistsByAuthorNameAndTitle(authorName, bookTitle);

        //then
        assertEquals("Andrzej Sapkowski", checkedBook.getAuthor().getName());
        assertEquals("SuperNowa", checkedBook.getPublisher().getName());
        assertEquals("Warsaw", checkedBook.getPublisher().getCity());
        assertEquals("The Witcher", checkedBook.getTitle());
        assertEquals(1990, checkedBook.getPublicationYear());
        assertNotNull(checkedBook.getImage());
    }


    @Test
    void prepareBookToProcessMethodReturnsThatBookWhenAuthorNotExistsAndPublisherExists() {

        //given
        String authorName = book.getAuthor().getName();
        Publisher publisher = book.getPublisher();

        given(authorService.getAuthorByName(authorName))
                .willThrow(ResourceNotFoundException.class);
        given(publisherService.getPublisherByNameAndCity(publisher.getName(), publisher.getCity()))
                .willReturn(publisher);

        //when
        Book preparedBook = bookService
                .prepareBookToProcess(book, book, authorName, bookImgMultipartFile);

        //then
        assertEquals("Andrzej Sapkowski", preparedBook.getAuthor().getName());
        assertEquals("SuperNowa", preparedBook.getPublisher().getName());
        assertEquals("Warsaw", preparedBook.getPublisher().getCity());
        assertEquals("The Witcher", preparedBook.getTitle());
        assertEquals(1990, preparedBook.getPublicationYear());
        assertNotNull(preparedBook.getImage());
    }

    @Test
    void prepareBookToProcessMethodReturnsThatBookWhenAuthorAndPublisherExist() {

        //given
        Author author = book.getAuthor();
        String authorName = author.getName();
        Publisher publisher = book.getPublisher();

        given(authorService.getAuthorByName(authorName)).willReturn(author);
        given(publisherService.getPublisherByNameAndCity(publisher.getName(), publisher.getCity()))
                .willReturn(publisher);

        //when
        Book preparedBook = bookService.prepareBookToProcess(book, book, authorName, bookImgMultipartFile);

        //then
        assertEquals("Andrzej Sapkowski", preparedBook.getAuthor().getName());
        assertEquals("SuperNowa", preparedBook.getPublisher().getName());
        assertEquals("Warsaw", preparedBook.getPublisher().getCity());
        assertEquals("The Witcher", preparedBook.getTitle());
        assertEquals(1990, preparedBook.getPublicationYear());
        assertNotNull(preparedBook.getImage());
    }

    @Test
    void prepareBookToProcessMethodReturnsThatBookWhenAuthorAndPublisherNotExist() {

        //given
        Author author = book.getAuthor();
        String authorName = author.getName();
        Publisher publisher = book.getPublisher();

        given(authorService.getAuthorByName(authorName))
                .willThrow(ResourceNotFoundException.class);
        given(publisherService.getPublisherByNameAndCity(publisher.getName(), publisher.getCity()))
                .willThrow(ResourceNotFoundException.class);

        //when
        Book preparedBook = bookService.prepareBookToProcess(book, book, authorName, bookImgMultipartFile);

        //then
        assertEquals("Andrzej Sapkowski", preparedBook.getAuthor().getName());
        assertEquals("SuperNowa", preparedBook.getPublisher().getName());
        assertEquals("Warsaw", preparedBook.getPublisher().getCity());
        assertEquals("The Witcher", preparedBook.getTitle());
        assertEquals(1990, preparedBook.getPublicationYear());
        assertNotNull(preparedBook.getImage());
    }

    @Test
    void prepareBookToProcessMethodReturnsThatBookWhenAuthorNotExistAndPublisherExist() {

        //given
        Author author = book.getAuthor();
        String authorName = author.getName();
        Publisher publisher = book.getPublisher();

        given(authorService.getAuthorByName(authorName))
                .willThrow(ResourceNotFoundException.class);
        given(publisherService
                .getPublisherByNameAndCity(publisher.getName(), publisher.getCity()))
                .willReturn(publisher);

        //when
        Book preparedBook = bookService
                .prepareBookToProcess(book, book, authorName, bookImgMultipartFile);

        //then
        assertEquals("Andrzej Sapkowski", preparedBook.getAuthor().getName());
        assertEquals("SuperNowa", preparedBook.getPublisher().getName());
        assertEquals("Warsaw", preparedBook.getPublisher().getCity());
        assertEquals("The Witcher", preparedBook.getTitle());
        assertEquals(1990, preparedBook.getPublicationYear());
        assertNotNull(preparedBook.getImage());
    }


    @Test
    void createBookWithNoImageReturnsThatBookWithEmptyImage() {

        //given
        final MultipartFile mockFile = mock(MultipartFile.class);
        var author = new Author("Andrzej Sapkowski");
        var publisher = new Publisher("SuperNowa", "Warsaw");
        Book book = new Book(author, "The Witcher", publisher, 1990);
        book.setId(1L);


        given(mockFile.getOriginalFilename()).willReturn("");
        given(mockFile.isEmpty()).willReturn(true);
        given(authorService.getAuthorByName(author.getName())).willReturn(author);
        given(publisherService.getPublisherByNameAndCity(publisher.getName(), publisher.getCity()))
                .willReturn(publisher);
        given(bookRepository.save(any(Book.class))).willReturn(book);

        //when
        Book createdBook = bookService.createBook(book, mockFile);

        //then
        assertEquals(1L, createdBook.getId());
        assertEquals("Andrzej Sapkowski", createdBook.getAuthor().getName());
        assertEquals("SuperNowa", createdBook.getPublisher().getName());
        assertEquals("Warsaw", createdBook.getPublisher().getCity());
        assertEquals("The Witcher", createdBook.getTitle());
        assertEquals(1990, createdBook.getPublicationYear());
        assertNull(createdBook.getImage());
    }


    @Test
    void updateBookWithNotImageChangeReturnsBookWithSameImage() throws IOException {

        //given
        var author = new Author("Dmitrij Gluchowski");
        var publisher = new Publisher("Insignis Media", "Cracow");
        var newBook = new Book(author, "Metro 2033", publisher, 2010);
        newBook.setId(1L);
        newBook.setImage(Base64.getEncoder()
                .encodeToString(bookImgMultipartFile.getBytes()));

        given(authorService.getAuthorByName(author.getName())).willReturn(author);
        given(publisherService.getPublisherByNameAndCity(publisher.getName(), publisher.getCity()))
                .willReturn(publisher);
        given(bookRepository
                .findByAuthorNameAndTitle(author.getName(), newBook.getTitle()))
                .willReturn(Optional.of(book));
        given(bookRepository.findById(book.getId())).willReturn(Optional.of(book));
        given(bookRepository.save(book)).willReturn(book);


        //when
        Book updatedBook = bookService.updateBook(1L, newBook, bookImgMultipartFile);

        //then
        assertEquals(1L, updatedBook.getId());
        assertEquals("Dmitrij Gluchowski", updatedBook.getAuthor().getName());
        assertEquals("Insignis Media", updatedBook.getPublisher().getName());
        assertEquals("Cracow", updatedBook.getPublisher().getCity());
        assertEquals("Metro 2033", updatedBook.getTitle());
        assertEquals(2010, updatedBook.getPublicationYear());
        assertEquals(updatedBook.getImage(), book.getImage());
    }

    @Test
    void updateToBookWithDifferentImageReturnsBookWithChangedImage() throws IOException {

        //given
        var newBookImgFile = new File("src/test/resources/static/img/bookCover/metro.jpg");
        var newBookImgMultipartFile = new MockMultipartFile("metro.jpg", new FileInputStream(newBookImgFile));
        Author author = book.getAuthor();
        Publisher publisher = book.getPublisher();
        String oldImage = book.getImage();

        given(authorService.getAuthorByName(author.getName())).willReturn(author);
        given(publisherService.getPublisherByNameAndCity(publisher.getName(), publisher.getCity()))
                .willReturn(publisher);
        given(bookRepository
                .findByAuthorNameAndTitle(author.getName(), book.getTitle()))
                .willReturn(Optional.of(book));
        given(bookRepository.findById(book.getId())).willReturn(Optional.of(book));
        given(bookRepository.save(book)).willReturn(book);


        //when
        Book updatedBook = bookService.updateBook(1L, book, newBookImgMultipartFile);

        //then
        assertEquals(1L, updatedBook.getId());
        assertEquals("Andrzej Sapkowski", updatedBook.getAuthor().getName());
        assertEquals("SuperNowa", updatedBook.getPublisher().getName());
        assertEquals("Warsaw", updatedBook.getPublisher().getCity());
        assertEquals("The Witcher", updatedBook.getTitle());
        assertEquals(1990, updatedBook.getPublicationYear());
        assertNotNull(updatedBook.getImage());
        assertNotEquals(updatedBook.getImage(), oldImage);
    }
}