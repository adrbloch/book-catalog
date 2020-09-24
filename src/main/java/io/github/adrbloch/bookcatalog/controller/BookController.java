package io.github.adrbloch.bookcatalog.controller;

import io.github.adrbloch.bookcatalog.domain.Book;
import io.github.adrbloch.bookcatalog.exception.ResourceAlreadyExistsException;
import io.github.adrbloch.bookcatalog.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {

    BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;

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
    public String saveBook(@ModelAttribute("book") @Valid Book bookToSave,
                           BindingResult bindingResult,
                           Model model) {

        if (bindingResult.hasErrors())
            return "add";

        try {
            bookService.createBook(bookToSave);
        } catch (ResourceAlreadyExistsException e){
            model.addAttribute("occurredException", true);
            model.addAttribute("exceptionMessage", e.getMessage());
            return "add";
        }
        return "redirect:/books/catalog";
    }

    @PostMapping("/save/{id}")
    public String updateBook(@PathVariable("id") Long id,
                             @ModelAttribute("book") @Valid Book bookToUpdate,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors())
            return "edit";

        try {
            bookService.updateBook(id, bookToUpdate);
        } catch (ResourceAlreadyExistsException e){
            model.addAttribute("occurredException", true);
            model.addAttribute("exceptionMessage", e.getMessage());
            return "edit";
        }
        return "redirect:/books/catalog";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBookById(id);
        return "redirect:/books/catalog";
    }

}

