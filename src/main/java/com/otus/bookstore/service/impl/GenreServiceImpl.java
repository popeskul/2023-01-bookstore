package com.otus.bookstore.service.impl;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Genre;
import com.otus.bookstore.repository.GenreRepository;
import com.otus.bookstore.service.GenreService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {
    private static final Logger LOGGER = LogManager.getLogger(GenreServiceImpl.class);

    public static final String ERROR_SAVING_GENRE = "Error saving genre %s";

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    @Transactional
    public Genre save(Genre genre) {
        try {
            return genreRepository.save(genre);
        } catch (DataAccessException e) {
            LOGGER.error("Error saving genre {}", genre, e);
            throw new EntitySaveException(String.format(ERROR_SAVING_GENRE, genre));
        }
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        genreRepository.deleteById(id);
    }

    @Override
    public Optional<Genre> getById(long id) {
        return genreRepository.findById(id);
    }

    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }
}
