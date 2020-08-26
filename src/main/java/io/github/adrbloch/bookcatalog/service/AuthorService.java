package io.github.adrbloch.bookcatalog.service;

import io.github.adrbloch.bookcatalog.domain.Author;
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

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author getAuthor(Long id) throws ResourceNotFoundException {
        logger.info("Get author with id: {}", id);
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id {" +id+ "} not found!"));
    }

    public List<Author> getAllAuthors() {
        logger.info("Get all authors");
        return authorRepository.findAll();
    }

    public Author createAuthor(Author author) {
        logger.info("Create author...");
        return authorRepository.save(author);
    }

    public Author updateAuthor(Author author, Long id) throws ResourceNotFoundException {
        logger.info("Update author with id: {}", id);

        Author authorToUpdate = getAuthor(id);
        authorToUpdate.setName(author.getName());
        return authorRepository.save(authorToUpdate);
    }

    public Author deleteAuthor(Long id) throws ResourceNotFoundException {
        logger.warn("Delete author with id: {}", id);
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
            return authorRepository.findById( id ).get();
        }
        else throw new ResourceNotFoundException("Author with id {" +id+ "} not found!");
    }


}




