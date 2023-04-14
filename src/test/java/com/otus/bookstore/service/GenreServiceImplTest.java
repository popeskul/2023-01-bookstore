package com.otus.bookstore.service;

import com.github.cloudyrock.spring.v5.EnableMongock;
import com.otus.bookstore.model.Genre;
import com.otus.bookstore.service.impl.GenreServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
@EnableMongock
@ComponentScan("com.otus.bookstore")
@ActiveProfiles("test")
@Import({GenreServiceImpl.class})
public class GenreServiceImplTest {
    private static final String name = "Genre1";
    private static final String name2 = "Genre2";

    @Autowired
    private GenreService genreService;

    private final Genre unsavedValidGenre = Genre.builder().name(name).build();

    @Test
    void shouldCreateGenre() {
        Genre genre = unsavedValidGenre.toBuilder().build();

        Genre createdGenre = genreService.save(genre);

        assertThat(createdGenre).isNotNull();
        assertThat(createdGenre.getId().length()).isPositive();

        Optional<Genre> actual = genreService.getById(createdGenre.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo(name);
    }

    // should not create genre with empty name
    @Test
    void shouldNotCreateGenreWithEmptyName() {
        Genre genre = unsavedValidGenre.toBuilder().name(null).build();

        assertThatThrownBy(() -> genreService.save(genre))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldUpdateGenre() {
        Genre genre = unsavedValidGenre.toBuilder().id("1").build();
        Genre createdGenre = genreService.save(genre);

        assertThat(createdGenre).isNotNull();
        assertThat(createdGenre.getId().length()).isPositive();

        Optional<Genre> savedGenre = genreService.getById(createdGenre.getId());

        assertThat(savedGenre).isPresent();
        assertThat(savedGenre.get().getName()).isEqualTo(name);

        Genre updatedGenre = savedGenre.get().toBuilder().name(name2).build();

        Genre saveUpdatedGenre = genreService.save(updatedGenre);

        Optional<Genre> actual = genreService.getById(saveUpdatedGenre.getId());

        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo(name2);
    }

    @Test
    void shouldDeleteById() {
        Genre genre = unsavedValidGenre.toBuilder().build();
        Genre createdGenre = genreService.save(genre);

        assertThat(createdGenre).isNotNull();
        assertThat(createdGenre.getId().length()).isPositive();

        genreService.deleteById(createdGenre.getId());

        Optional<Genre> actual = genreService.getById(createdGenre.getId());

        assertThat(actual).isNotPresent();
    }

    @Test
    void shouldGetById() {
        Genre genre = unsavedValidGenre.toBuilder().build();
        Genre createdGenre = genreService.save(genre);

        assertThat(createdGenre).isNotNull();
        assertThat(createdGenre.getId().length()).isPositive();

        Optional<Genre> actual = genreService.getById(createdGenre.getId());

        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo(name);
    }

    @Test
    void shouldGetAll() {
        assertThatThrownBy(() -> assertThat(genreService.getAll()).extracting(Genre::getName).contains(name, name2));

        Genre genre = unsavedValidGenre.toBuilder().build();
        Genre createdGenre = genreService.save(genre);

        assertThat(createdGenre).isNotNull();
        assertThat(createdGenre.getId().length()).isPositive();

        Genre genre2 = unsavedValidGenre.toBuilder().name(name2).build();
        Genre createdGenre2 = genreService.save(genre2);

        assertThat(createdGenre2).isNotNull();
        assertThat(createdGenre2.getId().length()).isPositive();

        assertThat(genreService.getAll()).extracting(Genre::getName).contains(name, name2);
    }
}
