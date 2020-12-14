package io.github.adrbloch.bookcatalog;

import io.github.adrbloch.bookcatalog.domain.Author;
import io.github.adrbloch.bookcatalog.domain.Book;
import io.github.adrbloch.bookcatalog.domain.Publisher;
import io.github.adrbloch.bookcatalog.domain.User;
import io.github.adrbloch.bookcatalog.service.BookService;
import io.github.adrbloch.bookcatalog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Component
public class DataLoader implements CommandLineRunner {

    private final BookService bookService;
    private final UserService userService;

    @Autowired
    public DataLoader(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws IOException {

        Author tolkien = new Author("J.R.R. Tolkien");
        Author rowling = new Author("J.K. Rowling");
        Author gluchowski = new Author("Dmitrij Gluchowski");

        Publisher alen = new Publisher("Allen and Unwin", "Manchester");
        Publisher bloomsbury = new Publisher("Bloomsbury Publishing", "London");
        Publisher insignis = new Publisher("Insignis Media", "Cracow");

        Book lotrBook = new Book(tolkien, "The Lord of the Rings", alen, 1954);
        Book hobbitBook = new Book(tolkien, "Hobbit", alen, 1937);
        Book hpPhilStoneBook = new Book(rowling, "Harry Potter and the Philosopher’s Stone", bloomsbury, 1998);
        Book hpDeathHallBook = new Book(rowling, "Harry Potter and the Deathly Hallows", bloomsbury, 2007);
        Book metroBook = new Book(gluchowski, "Metro 2033", insignis, 2010);

        File lotrFile = new File("src/main/resources/static/img/bookCover/lordOfTheRings.jpg");
        File hobbitFile = new File("src/main/resources/static/img/bookCover/hobbit.jpg");
        File hprPhilStoneFile = new File("src/main/resources/static/img/bookCover/harryPotterPhilosophersStone.jpg");
        File hpDeathHallFile = new File("src/main/resources/static/img/bookCover/harryPotterDeathlyHallows.jpg");
        File metroFile = new File("src/main/resources/static/img/bookCover/metro.jpg");

        MultipartFile lotrMultipartFile = new MockMultipartFile("lordOfTheRings.jpg", new FileInputStream(lotrFile));
        MultipartFile hobbitMultipartFile = new MockMultipartFile("hobbit.jpg", new FileInputStream(hobbitFile));
        MultipartFile hprPhilStoneMultipartFile = new MockMultipartFile("harryPotterPhilosophersStone.jpg", new FileInputStream(hprPhilStoneFile));
        MultipartFile hpDeathHallMultipartFile = new MockMultipartFile("harryPotterDeathlyHallows.jpg", new FileInputStream(hpDeathHallFile));
        MultipartFile metroMultipartFile = new MockMultipartFile("metro.jpg", new FileInputStream(metroFile));

        bookService.createBook(lotrMultipartFile, lotrBook);
        bookService.createBook(hobbitMultipartFile, hobbitBook);
        bookService.createBook(hprPhilStoneMultipartFile, hpPhilStoneBook);
        bookService.createBook(hpDeathHallMultipartFile, hpDeathHallBook);
        bookService.createBook(metroMultipartFile, metroBook);

        User user = new User("a", "a", "a");
        userService.createUser(user);
    }

}