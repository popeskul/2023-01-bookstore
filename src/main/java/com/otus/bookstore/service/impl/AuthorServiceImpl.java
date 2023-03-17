package com.otus.bookstore.service.impl;

import com.otus.bookstore.exception.AuthorBadRequestException;
import com.otus.bookstore.model.Author;
import com.otus.bookstore.repository.AuthorRepository;
import com.otus.bookstore.service.AuthorService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Transactional
@Service("authorService")
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Optional<Integer> create(Author author) {
        authorRepository.save(author).orElseThrow(AuthorBadRequestException::new);
        return Optional.of(author.getId());
    }

    @Override
    public Optional<Author> getById(int id) {
        return authorRepository.findById(id);
    }

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    public void update(Author author) {
        authorRepository.save(author).orElseThrow(AuthorBadRequestException::new);
    }

    @Override
    public void deleteById(int id) {
        authorRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        authorRepository.deleteAll();
    }
}
