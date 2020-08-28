package io.github.adrbloch.bookcatalog;

import io.github.adrbloch.bookcatalog.domain.Author;
import io.github.adrbloch.bookcatalog.domain.Book;
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


        Author tolkien = new Author("J.R.R. Tolkien");
        Author rowling = new Author("J.K. Rowling");
        Author gluchowski = new Author("Dmitrij Gluchowski");

        Publisher alen = new Publisher("Allen and Unwin", "Manchester");
        Publisher bloomsbury = new Publisher("Bloomsbury Publishing","London");
        Publisher insignis = new Publisher("Insignis Media","Cracow");

        Book book1 = new Book(tolkien,"The Lord of the Rings",alen,1954);
        Book book2 = new Book(tolkien,"Hobbit",alen,1937);
        Book book3 = new Book(rowling,"Harry Potter and the Philosopher’s Stone",bloomsbury,1998);
        Book book4 = new Book(rowling,"Harry Potter and the Deathly Hallows",bloomsbury,2007);
        Book book5 = new Book(gluchowski,"Metro 2033", insignis,2010);

        publisherService.createPublisher(alen);
        publisherService.createPublisher(bloomsbury);
        publisherService.createPublisher(insignis);

        authorService.createAuthor(tolkien);
        authorService.createAuthor(rowling);
        authorService.createAuthor(gluchowski);

        bookService.createBook(book1);
        bookService.createBook(book2);
        bookService.createBook(book3);
        bookService.createBook(book4);
        bookService.createBook(book5);




    }

}