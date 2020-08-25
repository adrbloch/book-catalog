package io.github.adrbloch.bookcatalog.repository;

import io.github.adrbloch.bookcatalog.domain.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository <Publisher, Long> {
}
