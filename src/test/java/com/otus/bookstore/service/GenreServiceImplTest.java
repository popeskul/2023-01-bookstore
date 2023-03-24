package com.otus.bookstore.service;

import com.otus.bookstore.model.Genre;
import com.otus.bookstore.service.impl.GenreServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import({GenreServiceImpl.class})
public class GenreServiceImplTest {
    private static final String name = "Genre1";
    private static final String name2 = "Genre2";

    @Autowired
    private GenreService genreService;

    private Genre unsavedValidGenre = Genre.builder().id(0L).name(name).build();

    @Test
    void shouldCreateGenre() {
        Genre genre = unsavedValidGenre.toBuilder().build();

        Optional<Long> id = genreService.create(genre);

        assertThat(id).isPresent();
        assertThat(id.get()).isPositive();

        Optional<Genre> actual = genreService.getById(id.get());
        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo(name);
    }

    @Test
    void shouldUpdateGenre() {
        Genre genre = unsavedValidGenre.toBuilder().id(1L).build();
        Optional<Long> id = genreService.create(genre);

        assertThat(id).isPresent();
        assertThat(id.get()).isPositive();

        Optional<Genre> savedGenre = genreService.getById(id.get());

        assertThat(savedGenre).isPresent();
        assertThat(savedGenre.get().getName()).isEqualTo(name);

        Genre updatedGenre = savedGenre.get().toBuilder().name(name2).build();

        genreService.update(updatedGenre);

        Optional<Genre> actual = genreService.getById(id.get());

        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo(name2);
    }

    @Test
    void shouldDeleteById() {
        Genre genre = unsavedValidGenre.toBuilder().build();
        Optional<Long> id = genreService.create(genre);

        assertThat(id).isPresent();
        assertThat(id.get()).isPositive();

        genreService.deleteById(id.get());

        Optional<Genre> actual = genreService.getById(id.get());

        assertThat(actual).isNotPresent();
    }

    @Test
    void shouldGetById() {
        Genre genre = unsavedValidGenre.toBuilder().build();
        Optional<Long> id = genreService.create(genre);

        assertThat(id).isPresent();
        assertThat(id.get()).isPositive();

        Optional<Genre> actual = genreService.getById(id.get());

        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo(name);
    }

    @Test
    void shouldGetAll() {
        Genre genre = unsavedValidGenre.toBuilder().build();
        Optional<Long> id = genreService.create(genre);

        assertThat(id).isPresent();
        assertThat(id.get()).isPositive();

        Genre genre2 = unsavedValidGenre.toBuilder().name(name2).build();
        Optional<Long> id2 = genreService.create(genre2);

        assertThat(id2).isPresent();
        assertThat(id2.get()).isPositive();

        assertThat(genreService.getAll().containsAll(List.of(genre, genre2))).isTrue();
    }
}
