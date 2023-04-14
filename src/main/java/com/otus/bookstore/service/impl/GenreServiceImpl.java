package com.otus.bookstore.service.impl;

import com.otus.bookstore.model.Genre;
import com.otus.bookstore.repository.GenreRepository;
import com.otus.bookstore.service.GenreService;
import com.otus.bookstore.validator.ObjectValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl extends EntityServiceImpl<Genre> implements GenreService {
    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository, ObjectValidator validator) {
        super(genreRepository, validator);
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre save(Genre genre) {
        return super.save(genre);
    }

    @Override
    public void deleteById(String id) {
        genreRepository.deleteById(id);
    }

    @Override
    public Optional<Genre> getById(String id) {
        return genreRepository.findById(id);
    }

    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }
}
