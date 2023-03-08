package com.otus.bookstore.dao;

import com.otus.bookstore.dao.impl.GenreDaoJdbc;
import com.otus.bookstore.service.query.genre.GenreQueryServiceImpl;
import com.otus.bookstore.model.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({GenreDaoJdbc.class, GenreQueryServiceImpl.class})
class GenreDaoTest {
    private final String name = "Genre1";
    private final String name2 = "Genre2";
    final int id = 1;

    @Autowired
    private GenreDaoJdbc genreDao;

    @BeforeEach
    void setUp() {
    }

    @Test
    void insert() {
        Genre genre = Genre.builder()
                .name(name)
                .build();

        Optional<Integer> savedGenreId = genreDao.insert(genre);

        assertThat(savedGenreId).isNotNull();
        assertThat(savedGenreId.isPresent()).isTrue();

        Optional<Genre> savedGenre = genreDao.getById(savedGenreId.get());

        assertThat(savedGenre).isNotNull();
        assertThat(savedGenre.isPresent()).isTrue();

        assertThat(savedGenre.get().getId()).isEqualTo(savedGenreId.get());
        assertThat(savedGenre.get().getName()).isEqualTo(name);
    }

    @Test
    void update() {
        Optional<Genre> genre = genreDao.getById(id);

        assertThat(genre).isNotNull();
        assertThat(genre.isPresent()).isTrue();

        genre.get().toBuilder().name(name2).build();

        Genre dirtyUpdatedGenre = genre.get().toBuilder().name(name2).build();

        genreDao.update(dirtyUpdatedGenre);

        Optional<Genre> updatedGenre = genreDao.getById(id);

        assertThat(updatedGenre).isNotNull();
        assertThat(updatedGenre.isPresent()).isTrue();

        assertThat(updatedGenre.get().getId()).isEqualTo(id);
        assertThat(updatedGenre.get().getName()).isEqualTo(name2);
    }

    @Test
    void deleteById() {
        Optional<Genre> genre = genreDao.getById(id);

        assertThat(genre).isNotNull();
        assertThat(genre.isPresent()).isTrue();

        genreDao.deleteById(id);

        Optional<Genre> deletedGenre = genreDao.getById(id);

        assertThat(deletedGenre).isNotNull();
        assertThat(deletedGenre.isPresent()).isFalse();
    }

    @Test
    void getById() {
        // add new genre
        Genre genre = Genre.builder()
                .name(name)
                .build();

        Optional<Integer> savedGenreId = genreDao.insert(genre);

        assertThat(savedGenreId).isNotNull();
        assertThat(savedGenreId.isPresent()).isTrue();

        Optional<Genre> savedGenre = genreDao.getById(savedGenreId.get());

        assertThat(savedGenre).isNotNull();
        assertThat(savedGenre.isPresent()).isTrue();

        assertThat(savedGenre.get().getId()).isEqualTo(savedGenreId.get());
        assertThat(savedGenre.get().getName()).isEqualTo(name);

        Optional<Genre> genreById = genreDao.getById(id);

        assertThat(genreById).isNotNull();
        assertThat(genreById.isPresent()).isTrue();
    }

    @Test
    void getAll() {
        final int sizeBeforeTest = genreDao.getAll().size();

        // add new genre
        Genre genre = Genre.builder()
                .name(name)
                .build();

        Optional<Integer> savedGenreId = genreDao.insert(genre);

        assertThat(savedGenreId).isNotNull();
        assertThat(savedGenreId.isPresent()).isTrue();

        Optional<Genre> savedGenre = genreDao.getById(savedGenreId.get());

        assertThat(savedGenre).isNotNull();
        assertThat(savedGenre.isPresent()).isTrue();

        assertThat(savedGenre.get().getId()).isEqualTo(savedGenreId.get());
        assertThat(savedGenre.get().getName()).isEqualTo(name);

        Optional<Genre> genreById = genreDao.getById(id);

        assertThat(genreById).isNotNull();
        assertThat(genreById.isPresent()).isTrue();

        assertThat(genreDao.getAll().size()).isEqualTo(sizeBeforeTest + 1);
    }

    @Test
    void shouldDeleteAllGenres() {
        genreDao.deleteAll();
        assertThat(genreDao.getAll().size()).isEqualTo(0);
    }
}