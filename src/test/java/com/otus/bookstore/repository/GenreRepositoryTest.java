package com.otus.bookstore.repository;

import com.otus.bookstore.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class GenreRepositoryTest {
    private static final String name = "Genre1";
    private static final String name2 = "Genre2";

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final Genre validGenreWithId = Genre.builder()
            .id(1L)
            .name(name)
            .build();

    @Test
    public void shouldSaveGenre() {
        Genre genre = validGenreWithId.toBuilder().build();

        genreRepository.save(genre);

        Genre actual = entityManager.find(Genre.class, genre.getId());

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getId()).isGreaterThan(0);
    }

    @Test
    public void shouldUpdateGenre() {
        Genre genre = validGenreWithId.toBuilder().build();

        genreRepository.save(genre);

        Genre actual = entityManager.find(Genre.class, genre.getId());

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getId()).isGreaterThan(0);

        Genre dirty = actual.toBuilder().name(name2).build();

        genreRepository.save(dirty);

        Genre actualDirty = entityManager.find(Genre.class, genre.getId());

        assertThat(actualDirty).isNotNull();
        assertThat(actualDirty.getName()).isEqualTo(name2);
        assertThat(actualDirty.getId()).isGreaterThan(0);
    }

    @Test
    public void shouldGetAllGenres() {
        assertThat(genreRepository.findAll()).isNotNull();
    }

    @Test
    public void shouldFindById() {
        Genre genre = Genre.builder().name(name).id(1L).build();

        genreRepository.save(genre);

        Optional<Genre> actual = genreRepository.findById(genre.getId());

        assertThat(actual).isNotNull();
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getName()).isEqualTo(name);
        assertThat(actual.get().getId()).isGreaterThan(0);
    }

    @Test
    public void shouldNotFindGenreById() {
        Optional<Genre> actual = genreRepository.findById(-1L);
        assertThat(actual).isEmpty();
    }

    @Test
    public void shouldDeleteGenreById() {
        Genre genre = validGenreWithId.toBuilder().build();

        genreRepository.save(genre);

        genreRepository.deleteById(genre.getId());

        Optional<Genre> actual = genreRepository.findById(genre.getId());

        assertThat(actual).isNotNull();
        assertThat(actual.isPresent()).isFalse();
    }
}
