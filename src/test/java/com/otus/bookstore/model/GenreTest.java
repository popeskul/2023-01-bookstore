package com.otus.bookstore.model;

import com.github.cloudyrock.spring.v5.EnableMongock;
import com.otus.bookstore.repository.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataMongoTest
@EnableMongock
@ActiveProfiles("test")
class GenreTest {
    private final String id = "1";
    private final String id2 = "2";
    private final String title = "Fiction";

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void testInitialSavedData() {
        List<Genre> allGenres = genreRepository.findAll();

        assert allGenres.size() == 3;
    }

    @Test
    void testEqualsAndHashCode() {
        final Genre genre1 = Genre.builder()
                .id(id)
                .name(title)
                .build();

        final Genre genre1TheSame = Genre.builder()
                .id(id)
                .name(title)
                .build();

        final Genre genre2Different = Genre.builder()
                .id(id2)
                .name(title)
                .build();

        // Reflexivity
        assertEquals(genre1, genre1TheSame);

        // Symmetry
        assertEquals(genre1, genre1TheSame);
        assertEquals(genre1TheSame, genre1);

        // Transitivity
        assertNotEquals(genre1, genre2Different);
        assertNotEquals(genre2Different, genre1);
        assertEquals(genre1.getName(), genre1TheSame.getName());
        assertEquals(genre1.getId(), genre1TheSame.getId());

        // Inequality
        assertEquals(genre1, genre1TheSame);
        assertNotEquals(genre1, new Object());

        // toString
        assertEquals(genre1.toString(), genre1TheSame.toString());
    }
}