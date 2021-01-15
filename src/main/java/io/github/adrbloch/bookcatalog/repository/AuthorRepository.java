package io.github.adrbloch.bookcatalog.repository;

import io.github.adrbloch.bookcatalog.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository <Author, Long> {

    Optional<Author> findByName(String name);
}
