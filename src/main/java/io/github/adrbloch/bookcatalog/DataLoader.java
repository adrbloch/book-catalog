package io.github.adrbloch.bookcatalog;

import io.github.adrbloch.bookcatalog.model.Author;
import io.github.adrbloch.bookcatalog.model.Book;
import io.github.adrbloch.bookcatalog.model.Publisher;
import io.github.adrbloch.bookcatalog.service.BookService;
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

    @Autowired
    public DataLoader(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws IOException {

        Author tolkien = new Author("J.R.R. Tolkien");
        Author rowling = new Author("J.K. Rowling");
        Author gluchowski = new Author("Dmitrij Gluchowski");
        Author king = new Author("Stephen King");

        Publisher alen = new Publisher("Allen and Unwin", "Manchester");
        Publisher bloomsbury = new Publisher("Bloomsbury Publishing", "London");
        Publisher insignis = new Publisher("Insignis Media", "Cracow");
        Publisher scribner = new Publisher("Scribner", "New York City");

        Book lotrBook = new Book(tolkien, "The Lord of the Rings", alen, 1954);
        Book hobbitBook = new Book(tolkien, "Hobbit", alen, 1937);
        Book hpPhilStoneBook = new Book(rowling, "Harry Potter and the Philosopher’s Stone", bloomsbury, 1998);
        Book hpDeathHallBook = new Book(rowling, "Harry Potter and the Deathly Hallows", bloomsbury, 2007);
        Book metroBook = new Book(gluchowski, "Metro 2033", insignis, 2010);
        Book outsiderBook = new Book(king, "The Outsider", scribner, 2018);

        File lotrFile = new File("src/main/resources/static/img/bookCover/lordOfTheRings.jpg");
        File hobbitFile = new File("src/main/resources/static/img/bookCover/hobbit.jpg");
        File hprPhilStoneFile = new File("src/main/resources/static/img/bookCover/harryPotterPhilosophersStone.jpg");
        File hpDeathHallFile = new File("src/main/resources/static/img/bookCover/harryPotterDeathlyHallows.jpg");
        File metroFile = new File("src/main/resources/static/img/bookCover/metro.jpg");
        File outsiderFile = new File("src/main/resources/static/img/bookCover/outsider.jpg");

        MultipartFile lotrMultipartFile = new MockMultipartFile("lordOfTheRings.jpg", new FileInputStream(lotrFile));
        MultipartFile hobbitMultipartFile = new MockMultipartFile("hobbit.jpg", new FileInputStream(hobbitFile));
        MultipartFile hprPhilStoneMultipartFile = new MockMultipartFile("harryPotterPhilosophersStone.jpg", new FileInputStream(hprPhilStoneFile));
        MultipartFile hpDeathHallMultipartFile = new MockMultipartFile("harryPotterDeathlyHallows.jpg", new FileInputStream(hpDeathHallFile));
        MultipartFile metroMultipartFile = new MockMultipartFile("metro.jpg", new FileInputStream(metroFile));
        MultipartFile outsiderMultipartFile = new MockMultipartFile("outsider.jpg", new FileInputStream(outsiderFile));

        bookService.createBook(hpDeathHallBook, hpDeathHallMultipartFile);
        bookService.createBook(hpPhilStoneBook, hprPhilStoneMultipartFile);
        bookService.createBook(hobbitBook, hobbitMultipartFile);
        bookService.createBook(metroBook, metroMultipartFile);
        bookService.createBook(lotrBook, lotrMultipartFile);
        bookService.createBook(outsiderBook, outsiderMultipartFile);

    }
}