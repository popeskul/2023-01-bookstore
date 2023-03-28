package com.otus.bookstore.service.impl;

import com.otus.bookstore.model.Author;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.model.Genre;
import com.otus.bookstore.service.AuthorService;
import com.otus.bookstore.service.BookCliService;
import com.otus.bookstore.service.BookService;
import com.otus.bookstore.service.GenreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BookCliServiceImpl implements BookCliService {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookCliServiceImpl(AuthorService authorService, GenreService genreService, BookService bookService) {
        this.authorService = authorService;
        this.genreService = genreService;
        this.bookService = bookService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAll() {
        return bookService.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> getById(long id) {
        return bookService.findById(id);
    }

    @Override
    @Transactional
    public Optional<Long> create(String title, String description, BigDecimal price, long authorId, long genreId) {
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

        final Book savedBook = bookService.create(book);

        return Optional.of(savedBook.getId());
    }

    @Override
    @Transactional
    public Book update(long id, String title, String description, BigDecimal price, long authorId, long genreId) {
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

        return bookService.update(book);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookService.deleteById(id);
    }
}
