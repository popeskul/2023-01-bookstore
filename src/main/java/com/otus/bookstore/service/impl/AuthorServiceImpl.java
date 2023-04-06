package com.otus.bookstore.service.impl;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Author;
import com.otus.bookstore.repository.AuthorRepository;
import com.otus.bookstore.service.AuthorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional
    public Author save(Author author) {
        try {
            return authorRepository.save(author);
        } catch (Exception e) {
            throw new EntitySaveException(author, e);
        }
    }

    @Override
    public Optional<Author> findById(long id) {
        return authorRepository.findById(id);
    }

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        authorRepository.deleteById(id);
    }
}
