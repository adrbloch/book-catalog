package io.github.adrbloch.bookcatalog;

import io.github.adrbloch.bookcatalog.domain.Publisher;
import io.github.adrbloch.bookcatalog.service.AuthorService;
import io.github.adrbloch.bookcatalog.service.BookService;
import io.github.adrbloch.bookcatalog.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private BookService bookService;
    private AuthorService authorService;
    private PublisherService publisherService;

    @Autowired
    public DataLoader(BookService bookService, AuthorService authorService, PublisherService publisherService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.publisherService = publisherService;
    }

    @Override
    public void run(String... args) throws Exception {
//       publisherService.createPublisher(new Publisher());

    }

}