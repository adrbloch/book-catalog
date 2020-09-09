package io.github.adrbloch.bookcatalog.repository;

import io.github.adrbloch.bookcatalog.domain.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository <Publisher, Long> {

    Optional<Publisher> findByNameAndCity(String name, String city);
}
