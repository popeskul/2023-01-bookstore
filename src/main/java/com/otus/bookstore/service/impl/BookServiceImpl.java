package com.otus.bookstore.service.impl;

import com.otus.bookstore.exception.AuthorNotFoundException;
import com.otus.bookstore.exception.BookBadRequestException;
import com.otus.bookstore.exception.BookErrorSavedException;
import com.otus.bookstore.exception.GenreNotFoundException;
import com.otus.bookstore.model.Author;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.model.Genre;
import com.otus.bookstore.repository.BookRepository;
import com.otus.bookstore.service.AuthorService;
import com.otus.bookstore.service.BookService;
import com.otus.bookstore.service.GenreService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, GenreService genreService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getById(int id) {
        return bookRepository.findById(id);
    }

    @Override
    public Optional<Integer> create(String title, String description, BigDecimal price, int authorId, int genreId) {
        Author author = authorService.getById(authorId).orElseThrow(AuthorNotFoundException::new);

        Genre genre = genreService.getById(genreId).orElseThrow(GenreNotFoundException::new);

        Book book = Book.builder()
                .title(title)
                .description(description)
                .price(price)
                .author(author)
                .genre(genre)
                .build();

        bookRepository.save(book).orElseThrow(BookErrorSavedException::new);
        return Optional.of(book.getId());
    }

    @Override
    public void update(int id, String title, String description, BigDecimal price, int authorId, int genreId) {
        if (id <= 0) {
            throw new BookBadRequestException();
        }

        Author author = authorService.getById(authorId).orElseThrow(AuthorNotFoundException::new);

        Genre genre = genreService.getById(genreId).orElseThrow(GenreNotFoundException::new);

        Book book = Book.builder()
                .id(id)
                .title(title)
                .description(description)
                .price(price)
                .author(author)
                .genre(genre)
                .build();

        bookRepository.save(book).orElseThrow(BookErrorSavedException::new);
    }

    @Override
    public void deleteById(int id) {
        bookRepository.deleteById(id);
    }
}
