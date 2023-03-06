package com.otus.bookstore.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
class AuthorTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testInitialSavedData() {
        List<Author> allAuthors = entityManager.getEntityManager()
                .createQuery("select a from Author a", Author.class)
                .getResultList();

        assert allAuthors.size() == 2;
    }

    @Test
    void testEqualsAndHashCode() {
        Author author1 = Author.builder().id(1).name("John Doe").email("john.doe@example.com").build();
        Author author2 = Author.builder().id(1).name("John Doe").email("john.doe@example.com").build();
        Author author3 = Author.builder().id(3).name("Jane Smith").email("jane.smith@example.com").build();

        // Reflexivity
        assertEquals(author1, author1);

        // Symmetry
        assertEquals(author1, author2);
        assertEquals(author2, author1);
        assertNotEquals(author1, author3);
        assertEquals(author1.hashCode(), author2.hashCode());

        // Transitivity
        assertEquals(author1, author2);
        assertNotEquals(author2, author3);
        assertNotEquals(author1, author3);

        // Inequality
        assertEquals(author1, author2);
        assertNotEquals(author1, author3);
        assertNotEquals(author1, null);
        assertNotEquals(author1, new Object());
        assertNotEquals(author1.getName(), author3.getName());
        assertNotEquals(author1.getEmail(), author3.getEmail());

        // toString
        assertEquals(author1.toString(), "Author(id=1, name=John Doe, email=john.doe@example.com)");
    }
}