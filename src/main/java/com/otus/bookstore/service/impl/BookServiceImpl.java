package com.otus.bookstore.service.impl;

import com.otus.bookstore.model.Book;
import com.otus.bookstore.repository.BookRepository;
import com.otus.bookstore.service.BookService;
import com.otus.bookstore.validator.ObjectValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl extends EntityServiceImpl<Book> implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository, ObjectValidator validator) {
        super(bookRepository, validator);
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        return super.save(book);
    }

    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }
}
