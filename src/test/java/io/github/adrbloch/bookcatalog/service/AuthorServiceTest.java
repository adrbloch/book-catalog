package io.github.adrbloch.bookcatalog.service;

import io.github.adrbloch.bookcatalog.model.Author;
import io.github.adrbloch.bookcatalog.exception.ResourceAlreadyExistsException;
import io.github.adrbloch.bookcatalog.exception.ResourceNotFoundException;
import io.github.adrbloch.bookcatalog.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;
    @InjectMocks
    private AuthorService authorService;


    private Author initializeAuthor() {

        Author author = new Author("George Orwell");
        author.setId(1L);
        return author;
    }


    @Test
    void createAlreadyExistingAuthorThrowsException() {

        //given
        Author author = new Author();
        given(authorRepository.findByName(author.getName())).willReturn(Optional.of(author));

        //when
        //then
        assertThrows(ResourceAlreadyExistsException.class, () -> authorService.createAuthor(author));
    }

    @Test
    void returnAuthorAfterCreate() {

        //given
        Author author = initializeAuthor();
        given(authorRepository.findByName(author.getName())).willReturn(Optional.empty());
        given(authorRepository.save(author)).willReturn(author);

        //when
        Author createdAuthor = authorService.createAuthor(author);
        //then
        assertEquals(1L, createdAuthor.getId());
        assertEquals("George Orwell", createdAuthor.getName());
    }

    @Test
    void returnAuthorAfterUpdate() {

        //given
        Author author = initializeAuthor();
        given(authorRepository.findById(author.getId())).willReturn(Optional.of(author));
        given(authorRepository.save(author)).willReturn(author);

        //when
        Author updatedAuthor = authorService.updateAuthor(author.getId(), author);

        //then
        assertEquals(1L, updatedAuthor.getId());
        assertEquals("George Orwell", updatedAuthor.getName());
    }

    @Test
    void returnAuthorAfterDelete() {

        //given
        Author author = initializeAuthor();
        given(authorRepository.findById(author.getId())).willReturn(Optional.of(author));
//        given(authorRepository.deleteById(author);).willReturn(author);

        //when
        Author deletedAuthor = authorService.deleteAuthorById(author.getId());

        //then
        assertEquals(1L, deletedAuthor.getId());
        assertEquals("George Orwell", deletedAuthor.getName());
    }

    @Test
    void ifAuthorNotExistsByIdThrowException() {

        //given
        Author author = new Author();
        Long id = author.getId();
        given(authorRepository.findById(id)).willReturn(Optional.empty());

        //when
        //then
        assertThrows(ResourceNotFoundException.class, () -> authorService.returnAuthorIfExistsById(id));
    }

    @Test
    void ifAuthorExistsByIdReturnThatAuthor() {

        //given
        Author author = initializeAuthor();
        Long id = author.getId();
        given(authorRepository.findById(id)).willReturn(Optional.of(author));

        //when
        Author checkedAuthor = authorService.returnAuthorIfExistsById(id);

        //then
        assertEquals(1L, checkedAuthor.getId());
        assertEquals("George Orwell", checkedAuthor.getName());
    }

    @Test
    void ifAuthorNotExistsByNameThrowException() {

        //given
        Author author = new Author();
        String authorName = author.getName();
        given(authorRepository.findByName(authorName)).willReturn(Optional.empty());

        //when
        //then
        assertThrows(ResourceNotFoundException.class, () -> authorService
                .returnAuthorIfExistsByName(authorName));
    }

    @Test
    void IfAuthorExistsByNameReturnThatAuthor() {

        //given
        Author author = initializeAuthor();
        String authorName = author.getName();
        given(authorRepository.findByName(authorName)).willReturn(Optional.of(author));

        //when
        Author checkedAuthor = authorService.returnAuthorIfExistsByName(authorName);

        //then
        assertEquals(1L, checkedAuthor.getId());
        assertEquals("George Orwell", checkedAuthor.getName());
    }

}