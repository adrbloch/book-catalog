package io.github.adrbloch.bookcatalog.repository;

import io.github.adrbloch.bookcatalog.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository <Book, Long> {

    Optional<Book> findByAuthorNameAndTitle(String authorName, String title);

}
