package com.otus.bookstore.service.impl;

import com.otus.bookstore.model.Author;
import com.otus.bookstore.repository.AuthorRepository;
import com.otus.bookstore.service.AuthorService;
import com.otus.bookstore.validator.ObjectValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl extends EntityServiceImpl<Author> implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository, ObjectValidator validator) {
        super(authorRepository, validator);
        this.authorRepository = authorRepository;
    }

    @Override
    public Author save(Author author) {
        return super.save(author);
    }

    @Override
    public Optional<Author> findById(String id) {
        return authorRepository.findById(id);
    }

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        authorRepository.deleteById(id);
    }
}
