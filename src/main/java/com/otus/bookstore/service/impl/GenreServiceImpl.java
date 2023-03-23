package com.otus.bookstore.service.impl;

import com.otus.bookstore.model.Genre;
import com.otus.bookstore.repository.GenreRepository;
import com.otus.bookstore.service.GenreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {
    public static final String ERROR_ILLEGAL_ARGUMENT = "Genre id must be greater than 0";

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    @Transactional
    public Optional<Integer> create(Genre genre) {
        genreRepository.save(genre);
        return Optional.of(genre.getId());
    }

    @Override
    @Transactional
    public void update(Genre genre) {
        genreRepository.save(genre);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        genreRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Genre> getById(int id) {
        if (id == 0) {
            throw new InvalidParameterException(ERROR_ILLEGAL_ARGUMENT);
        }

        return genreRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }
}
