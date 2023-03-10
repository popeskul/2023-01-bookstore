package com.otus.bookstore.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
class GenreTest {
    private final int id = 1;
    private final int id2 = 2;
    private final String title = "Fiction";

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testInitialSavedData() {
        List<Genre> allGenres = entityManager.getEntityManager()
                .createQuery("select g from Genre g", Genre.class)
                .getResultList();

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