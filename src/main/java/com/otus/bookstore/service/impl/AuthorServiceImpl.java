package com.otus.bookstore.service.impl;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Author;
import com.otus.bookstore.repository.AuthorRepository;
import com.otus.bookstore.service.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {
    public static final String ERROR_AUTHOR_NOT_FOUND = "Author with id %d not found";
    public static final String ERROR_AUTHOR_NULL = "Author is null";
    public static final String ERROR_AUTHOR_ALREADY_EXISTS = "Author already exists";

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional
    public Author create(Author author) {
        if (author == null) {
            throw new IllegalArgumentException(ERROR_AUTHOR_NULL);
        }

        if (author.getId() != 0) {
            throw new EntitySaveException(ERROR_AUTHOR_ALREADY_EXISTS);
        }

        return authorRepository.save(author);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Author> findById(long id) {
        if (id == 0) {
            throw new EntityNotFoundException(String.format(ERROR_AUTHOR_NOT_FOUND, id));
        }

        return authorRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    @Transactional
    public Author update(Author author) {
        if (author == null) {
            throw new IllegalArgumentException(ERROR_AUTHOR_NULL);
        }

        if (author.getId() == 0) {
            throw new EntitySaveException(author);
        }

        authorRepository.findById(author.getId()).orElseThrow();
        return authorRepository.save(author);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        authorRepository.deleteById(id);
    }
}
