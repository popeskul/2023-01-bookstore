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

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional
    public Optional<Integer> create(Author author) {
        try {
            Optional<Author> savedAuthor = authorRepository.save(author);
            if (savedAuthor.isPresent()) {
                return Optional.of(savedAuthor.get().getId());
            } else {
                throw new EntitySaveException(author);
            }
        } catch (RuntimeException e) {
            throw new EntitySaveException(author, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Author> getById(int id) {
        try {
            return authorRepository.findById(id);
        } catch (RuntimeException e) {
            throw new EntityNotFoundException(String.format(ERROR_AUTHOR_NOT_FOUND, id), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    @Transactional
    public void update(Author author) {
        Optional<Author> existingAuthor = authorRepository.findById(author.getId());
        if (existingAuthor.isPresent()) {
            authorRepository.save(author);
        } else {
            throw new EntitySaveException(author);
        }
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        authorRepository.deleteById(id);
    }
}
