package io.github.adrbloch.bookcatalog.controller;

import io.github.adrbloch.bookcatalog.domain.Author;
import io.github.adrbloch.bookcatalog.domain.Book;
import io.github.adrbloch.bookcatalog.domain.Publisher;
import io.github.adrbloch.bookcatalog.service.AuthorService;
import io.github.adrbloch.bookcatalog.service.BookService;
import io.github.adrbloch.bookcatalog.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    BookService bookService;
    AuthorService authorService;
    PublisherService publisherService;

    @Autowired
    public BookController(BookService bookService, AuthorService authorService, PublisherService publisherService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.publisherService = publisherService;
    }


    @GetMapping("/catalog")
    public String viewAll(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "catalog";
    }


    @GetMapping("/{id}")
    public String viewBook(@PathVariable("id") Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "book";
    }

    @GetMapping("/add")
    public String addBookForm(Model model) {
        Book newBook = new Book();
        model.addAttribute("book", newBook);
        return "add";
    }

    @GetMapping("/edit/{id}")
    public String editBookForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "edit";
    }

    @PostMapping("/save")
    public String saveBook(@ModelAttribute("book") Book bookToSave) {
        bookService.createBook(bookToSave);
        return "redirect:/books/catalog";
    }

    @PostMapping("/save/{id}")
    public String updateBook(@PathVariable("id") Long id, @ModelAttribute("book") Book bookToUpdate) {
        bookService.updateBook(id, bookToUpdate);
        return "redirect:/books/catalog";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBookById(id);
        return "redirect:/books/catalog";
    }

}

