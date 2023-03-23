package com.otus.bookstore.service.impl;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Author;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.model.Genre;
import com.otus.bookstore.repository.BookRepository;
import com.otus.bookstore.repository.impl.BookRepositoryJpa;
import com.otus.bookstore.service.AuthorService;
import com.otus.bookstore.service.BookService;
import com.otus.bookstore.service.GenreService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    public static final String ERROR_AUTHOR_NOT_FOUND = "Author not found with id: %d";
    public static final String ERROR_GENRE_NOT_FOUND = "Genre not found with id: %d";
    public static final String ERROR_ID_MUST_BE_GREATER_THAN_ZERO = "Id must be greater than 0";

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, GenreService genreService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> getById(long id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Long> create(String title, String description, BigDecimal price, int authorId, int genreId) {
        Author author = authorService.getById(authorId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(AuthorServiceImpl.ERROR_AUTHOR_NOT_FOUND, authorId)));

        Genre genre = genreService.getById(genreId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_GENRE_NOT_FOUND, genreId)));

        Book book = Book.builder()
                .title(title)
                .description(description)
                .price(price)
                .author(author)
                .genre(genre)
                .build();

        bookRepository.save(book)
                .orElseThrow(() -> new EntitySaveException(String.format(BookRepositoryJpa.ERROR_SAVE_BOOK, book)));

        return Optional.of(book.getId());
    }

    @Override
    @Transactional
    public void update(long id, String title, String description, BigDecimal price, int authorId, int genreId) {
        if (id <= 0) {
            throw new InvalidParameterException(ERROR_ID_MUST_BE_GREATER_THAN_ZERO);
        }

        Author author = authorService.getById(authorId).orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_AUTHOR_NOT_FOUND, authorId)));

        Genre genre = genreService.getById(genreId).orElseThrow(EntityNotFoundException::new);

        Book book = Book.builder()
                .id(id)
                .title(title)
                .description(description)
                .price(price)
                .author(author)
                .genre(genre)
                .build();

        bookRepository.save(book).orElseThrow(() -> new EntitySaveException(String.format(BookRepositoryJpa.ERROR_SAVE_BOOK, book)));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }
}
