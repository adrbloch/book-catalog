package io.github.adrbloch.bookcatalog.repository;

import io.github.adrbloch.bookcatalog.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository <Author, Long> {
    boolean existsByName(String name);
}
