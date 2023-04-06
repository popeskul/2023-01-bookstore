package com.otus.bookstore.service.impl;

import com.otus.bookstore.model.Author;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.model.Genre;
import com.otus.bookstore.service.AuthorService;
import com.otus.bookstore.service.CliBookService;
import com.otus.bookstore.service.BookService;
import com.otus.bookstore.service.GenreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CliBookServiceImpl implements CliBookService {

    public static final String ERROR_ID_MUST_BE_GREATER_THAN_0 = "Id must be greater than 0";

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    public CliBookServiceImpl(AuthorService authorService, GenreService genreService, BookService bookService) {
        this.authorService = authorService;
        this.genreService = genreService;
        this.bookService = bookService;
    }

    @Override
    public List<Book> getAll() {
        return bookService.findAll();
    }

    @Override
    public Optional<Book> getById(long id) {
        return bookService.findById(id);
    }

    @Override
    @Transactional
    public Book create(String title, String description, BigDecimal price, long authorId, long genreId) {
        Author author = authorService.findById(authorId).orElseThrow();

        Genre genre = genreService.getById(genreId).orElseThrow();

        Book book = Book.builder()
                .id(0L)
                .title(title)
                .description(description)
                .price(price)
                .author(author)
                .genre(genre)
                .build();

        return bookService.save(book);
    }

    @Override
    @Transactional
    public Book update(long id, String title, String description, BigDecimal price, long authorId, long genreId) {
        if (id == 0) {
            throw new IllegalArgumentException(ERROR_ID_MUST_BE_GREATER_THAN_0);
        }

        Author author = authorService.findById(authorId).orElseThrow();

        Genre genre = genreService.getById(genreId).orElseThrow();

        Book book = Book.builder()
                .id(id)
                .title(title)
                .description(description)
                .price(price)
                .author(author)
                .genre(genre)
                .build();

        return bookService.save(book);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookService.deleteById(id);
    }
}
