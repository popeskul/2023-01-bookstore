package com.otus.bookstore.service.impl;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.repository.BookRepository;
import com.otus.bookstore.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    public static final String ERROR_FIELD_MUST_BE_GREATER_THAN_ZERO = "ID must be greater than 0, but was %d";
    public static final String ERROR_BOOK_NOT_FOUND = "Book not found with id: %d";
    public static final String ERROR_UPDATE_BOOK = "Failed to update book with id: %d";
    public static final String ERROR_NOT_NULL_BOOK = "Book cannot be null";


    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public Book create(Book book) {
        if (book == null) {
            throw new InvalidParameterException(ERROR_NOT_NULL_BOOK);
        }

        if (book.getId() > 0) {
            throw new InvalidParameterException(String.format(ERROR_FIELD_MUST_BE_GREATER_THAN_ZERO, book.getId()));
        }

        return bookRepository.save(book).orElseThrow();
    }

    @Override
    @Transactional
    public Book update(Book book) {
        if (book == null) {
            throw new InvalidParameterException(ERROR_NOT_NULL_BOOK);
        }

        if (book.getId() <= 0) {
            throw new InvalidParameterException(String.format(ERROR_FIELD_MUST_BE_GREATER_THAN_ZERO, book.getId()));
        }

        Optional<Book> bookFromDb = bookRepository.findById(book.getId());

        if (bookFromDb.isEmpty()) {
            throw new InvalidParameterException(String.format(ERROR_BOOK_NOT_FOUND, book.getId()));
        }

        return bookRepository.save(book)
                .orElseThrow(() -> new EntitySaveException(String.format(ERROR_UPDATE_BOOK, book.getId())));
    }

    @Override
    public Optional<Book> findById(long id) {
        if (id <= 0) {
            throw new InvalidParameterException(String.format(ERROR_FIELD_MUST_BE_GREATER_THAN_ZERO, id));
        }

        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        if (id <= 0) {
            throw new InvalidParameterException(String.format(ERROR_FIELD_MUST_BE_GREATER_THAN_ZERO, id));
        }

        bookRepository.deleteById(id);
    }
}
