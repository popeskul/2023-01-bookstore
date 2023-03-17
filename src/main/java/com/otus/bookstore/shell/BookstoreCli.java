package com.otus.bookstore.shell;

import com.otus.bookstore.model.Book;
import com.otus.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class BookstoreCli {
    private final BookService bookService;

    @ShellMethod(value = "Get all books", key = {"book list"})
    public List<Book> getAllBooks() {
        return bookService.getAll();
    }

    @ShellMethod(value = "Get book by id", key = {"book find"})
    public Optional<Book> getBookById(
            @ShellOption(defaultValue = "id") String id
    ) {
        return bookService.getById(Integer.parseInt(id));
    }

    @ShellMethod(value = "Create book", key = {"book create"})
    public Optional<Integer> createBook(
            @ShellOption(defaultValue = "title") String title,
            @ShellOption(defaultValue = "description") String description,
            @ShellOption(defaultValue = "price") String price,
            @ShellOption(defaultValue = "authorId") String authorId,
            @ShellOption(defaultValue = "genreId") String genreId
    ) {
        return bookService.create(title, description, new BigDecimal(price), Integer.parseInt(authorId), Integer.parseInt(genreId));
    }

    @ShellMethod(value = "Update book", key = {"book update"})
    public void updateBook(
            @ShellOption(defaultValue = "id") String id,
            @ShellOption(defaultValue = "title") String title,
            @ShellOption(defaultValue = "description") String description,
            @ShellOption(defaultValue = "price") String price,
            @ShellOption(defaultValue = "authorId") String authorId,
            @ShellOption(defaultValue = "genreId") String genreId
    ) {
        bookService.update(
                Integer.parseInt(id), title, description, new BigDecimal(price),
                Integer.parseInt(authorId), Integer.parseInt(genreId)
        );
    }

    @ShellMethod(value = "Delete book by id", key = {"book delete"})
    public void deleteBookById(
            @ShellOption(defaultValue = "id") String id
    ) {
        bookService.deleteById(Integer.parseInt(id));
    }
}
