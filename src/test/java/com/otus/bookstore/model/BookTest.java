package com.otus.bookstore.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
class BookDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testInitialSavedData() {
        List<Book> allBooks = entityManager.getEntityManager()
                .createQuery("select b from Book b", Book.class)
                .getResultList();

        assert allBooks.size() == 4;
    }

    @Test
    void testEqualsAndHashCode() {
        final Author author = Author.builder().id(1).name("John Doe").email("some@mail.com").build();
        final Genre genre = Genre.builder().id(1).name("Fiction").build();

        final Book book1 = Book.builder()
                .id(1)
                .title("Book 1")
                .description("Description 1")
                .price(BigDecimal.valueOf(100))
                .author(author)
                .genre(genre)
                .build();

        final Book book1TheSame = Book.builder()
                .id(1)
                .title("Book 1")
                .description("Description 1")
                .price(BigDecimal.valueOf(100))
                .author(author)
                .genre(genre)
                .build();

        final Book book2Different = Book.builder()
                .id(2)
                .title("Book 1")
                .description("Description 1")
                .price(BigDecimal.valueOf(100))
                .author(author)
                .genre(genre)
                .build();

        // Reflexivity
        assertEquals(book1, book1TheSame);

        // Symmetry
        assertEquals(book1, book1TheSame);
        assertEquals(book1TheSame, book1);
        assertNotEquals(book1, book2Different);
        assertEquals(book1.hashCode(), book1TheSame.hashCode());
        assertEquals(book1.getTitle(), book1TheSame.getTitle());
        assertEquals(book1.getDescription(), book1TheSame.getDescription());
        assertEquals(book1.getPrice(), book1TheSame.getPrice());
        assertEquals(book1.getAuthor(), book1TheSame.getAuthor());
        assertEquals(book1.getGenre(), book1TheSame.getGenre());

        // Transitivity
        assertEquals(book1, book1TheSame);
        assertNotEquals(book1TheSame, book2Different);
        assertNotEquals(book1, book2Different);

        // Inequality
        assertEquals(book1, book1TheSame);
        assertNotEquals(book1, book2Different);
        assertNotEquals(book1, null);
        assertNotEquals(book1, new Object());
        assertNotEquals(book1.getId(), book2Different.getId());

        // toString
        assertEquals(book1.toString(), "Book{id=1, title='Book 1', description='Description 1', price=100, author=" + author + ", genre=" + genre + "}");
    }
}