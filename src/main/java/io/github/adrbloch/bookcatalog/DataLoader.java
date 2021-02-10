package io.github.adrbloch.bookcatalog;

import io.github.adrbloch.bookcatalog.model.Author;
import io.github.adrbloch.bookcatalog.model.Book;
import io.github.adrbloch.bookcatalog.model.Publisher;
import io.github.adrbloch.bookcatalog.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

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

        var tolkien = new Author("J.R.R. Tolkien");
        var rowling = new Author("J.K. Rowling");
        var gluchowski = new Author("Dmitrij Gluchowski");
        var king = new Author("Stephen King");

        var alen = new Publisher("Allen and Unwin", "Manchester");
        var bloomsbury = new Publisher("Bloomsbury Publishing", "London");
        var insignis = new Publisher("Insignis Media", "Cracow");
        var scribner = new Publisher("Scribner", "New York City");

        var lotrBook = new Book(tolkien, "The Lord of the Rings", alen, 1954);
        var hobbitBook = new Book(tolkien, "Hobbit", alen, 1937);
        var hpPhilStoneBook = new Book(rowling, "Harry Potter and the Philosopher’s Stone", bloomsbury, 1998);
        Book hpDeathHallBook = new Book(rowling, "Harry Potter and the Deathly Hallows", bloomsbury, 2007);
        var metroBook = new Book(gluchowski, "Metro 2033", insignis, 2010);
        var outsiderBook = new Book(king, "The Outsider", scribner, 2018);

        var lotrFile = new File("src/main/resources/static/img/bookCover/lordOfTheRings.jpg");
        var hobbitFile = new File("src/main/resources/static/img/bookCover/hobbit.jpg");
        var hprPhilStoneFile = new File("src/main/resources/static/img/bookCover/harryPotterPhilosophersStone.jpg");
        var hpDeathHallFile = new File("src/main/resources/static/img/bookCover/harryPotterDeathlyHallows.jpg");
        var metroFile = new File("src/main/resources/static/img/bookCover/metro.jpg");
        var outsiderFile = new File("src/main/resources/static/img/bookCover/outsider.jpg");

        var lotrMultipartFile = new MockMultipartFile("lordOfTheRings.jpg", new FileInputStream(lotrFile));
        var hobbitMultipartFile = new MockMultipartFile("hobbit.jpg", new FileInputStream(hobbitFile));
        var hprPhilStoneMultipartFile = new MockMultipartFile("harryPotterPhilosophersStone.jpg", new FileInputStream(hprPhilStoneFile));
        var hpDeathHallMultipartFile = new MockMultipartFile("harryPotterDeathlyHallows.jpg", new FileInputStream(hpDeathHallFile));
        var metroMultipartFile = new MockMultipartFile("metro.jpg", new FileInputStream(metroFile));
        var outsiderMultipartFile = new MockMultipartFile("outsider.jpg", new FileInputStream(outsiderFile));

        bookService.createBook(hpDeathHallBook, hpDeathHallMultipartFile);
        bookService.createBook(hpPhilStoneBook, hprPhilStoneMultipartFile);
        bookService.createBook(hobbitBook, hobbitMultipartFile);
        bookService.createBook(metroBook, metroMultipartFile);
        bookService.createBook(lotrBook, lotrMultipartFile);
        bookService.createBook(outsiderBook, outsiderMultipartFile);

    }
}