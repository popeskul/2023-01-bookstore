package com.otus.bookstore.shell;

import com.otus.bookstore.model.Book;
import com.otus.bookstore.service.CliBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class CliBookstore {
    private final CliBookService cliBookService;

    @ShellMethod(value = "Get all books", key = {"book list"})
    public List<Book> getAllBooks() {
        return cliBookService.getAll();
    }

    @ShellMethod(value = "Get book by id", key = {"book find"})
    public Optional<Book> getBookById(
            @ShellOption(defaultValue = "id") String id
    ) {
        return cliBookService.getById(id);
    }

    @ShellMethod(value = "Create book", key = {"book create"})
    public Book createBook(
            @ShellOption(defaultValue = "title") String title,
            @ShellOption(defaultValue = "description") String description,
            @ShellOption(defaultValue = "price") String price,
            @ShellOption(defaultValue = "authorId") String authorId,
            @ShellOption(defaultValue = "genreId") String genreId
    ) {
        return cliBookService.create(title, description, new BigDecimal(price), authorId, genreId);
    }

    @ShellMethod(value = "Update book", key = {"book update"})
    public Book updateBook(
            @ShellOption(defaultValue = "id") String id,
            @ShellOption(defaultValue = "title") String title,
            @ShellOption(defaultValue = "description") String description,
            @ShellOption(defaultValue = "price") String price,
            @ShellOption(defaultValue = "authorId") String authorId,
            @ShellOption(defaultValue = "genreId") String genreId
    ) {
        try {
            return cliBookService.update(id, title, description, new BigDecimal(price), authorId, genreId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ShellMethod(value = "Delete book by id", key = {"book delete"})
    public void deleteBookById(
            @ShellOption(defaultValue = "id") String id
    ) {
        cliBookService.deleteById(id);
    }
}
