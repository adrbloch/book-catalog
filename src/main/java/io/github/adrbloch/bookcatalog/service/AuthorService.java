package io.github.adrbloch.bookcatalog.service;

import io.github.adrbloch.bookcatalog.domain.Author;
import io.github.adrbloch.bookcatalog.exception.ResourceAlreadyExistsException;
import io.github.adrbloch.bookcatalog.exception.ResourceNotFoundException;
import io.github.adrbloch.bookcatalog.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    public static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService( AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author getAuthorById(Long id) {
        logger.info("Get author with id: {}", id);
        return checkIfExistsByIdAndReturnAuthor(id);
    }

    public Author getAuthorByName(String authorName) {
        logger.info("Get author with name: {}", authorName);
        return checkIfExistsByNameAndReturnAuthor(authorName);
    }

    public List<Author> getAllAuthors() {
        logger.info("Get all authors");
        return authorRepository.findAll();
    }

    public Author createAuthor(Author author) {
        logger.info("Create author...");
        if (authorRepository.findByName(author.getName()).isPresent())
            throw new ResourceAlreadyExistsException("Author already exists!");
        else
            return authorRepository.save(author);
    }

    public Author updateAuthor(Long id, Author author) {
        logger.info("Update author with id: {}", id);

        Author authorToUpdate = checkIfExistsByIdAndReturnAuthor(id);
        authorToUpdate.setName(author.getName());
        return authorRepository.save(authorToUpdate);
    }

    public Author deleteAuthorById(Long id) {
        logger.warn("Delete author with id: {}", id);
        Author authorToDelete = checkIfExistsByIdAndReturnAuthor(id);
        authorRepository.deleteById(id);
        return authorToDelete;
    }

    private Author checkIfExistsByIdAndReturnAuthor(Long id) {
        if (authorRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Author with id: {" + id + "} not found!");
        } else return authorRepository.findById(id).get();
    }

    private Author checkIfExistsByNameAndReturnAuthor(String authorName) {
        if (authorRepository.findByName(authorName).isEmpty()) {
            throw new ResourceNotFoundException("Author with name: {" + authorName + "} not found!");
        } else return authorRepository.findByName(authorName).get();
    }


}




