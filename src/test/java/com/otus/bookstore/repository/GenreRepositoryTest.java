package com.otus.bookstore.repository;

import com.otus.bookstore.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
@ComponentScan({"com.otus.bookstore.repository", "com.otus.bookstore.model"})
public class GenreRepositoryTest {
    private static final String name = "Genre1";
    private static final String name2 = "Genre2";

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final Genre validGenreWithId = Genre.builder()
            .name(name)
            .build();

    @Test
    public void shouldSaveGenre() {
        Genre genre = validGenreWithId.toBuilder().build();

        genreRepository.save(genre);

        Genre actual = mongoTemplate.findById(genre.getId(), Genre.class);

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getId().length()).isGreaterThan(0);
    }

    @Test
    public void shouldUpdateGenre() {
        Genre genre = validGenreWithId.toBuilder().build();

        genreRepository.save(genre);

        Genre actual = mongoTemplate.findById(genre.getId(), Genre.class);

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getId().length()).isGreaterThan(0);

        Genre dirty = actual.toBuilder().name(name2).build();

        genreRepository.save(dirty);

        Genre actualDirty = mongoTemplate.findById(dirty.getId(), Genre.class);

        assertThat(actualDirty).isNotNull();
        assertThat(actualDirty.getName()).isEqualTo(name2);
        assertThat(actualDirty.getId().length()).isGreaterThan(0);
    }

    @Test
    public void shouldGetAllGenres() {
        assertThat(genreRepository.findAll()).isNotNull();
    }

    @Test
    public void shouldFindById() {
        Genre genre = Genre.builder().name(name).build();

        genreRepository.save(genre);

        Optional<Genre> actual = genreRepository.findById(genre.getId());

        assertThat(actual).isNotNull();
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getName()).isEqualTo(name);
        assertThat(actual.get().getId().length()).isGreaterThan(0);
    }

    @Test
    public void shouldNotFindGenreById() {
        Optional<Genre> actual = genreRepository.findById("-1");
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
