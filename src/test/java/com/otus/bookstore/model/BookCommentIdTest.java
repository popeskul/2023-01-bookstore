package com.otus.bookstore.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
class BookCommentIdTest {
    private BookCommentId bookCommentIdTemplate;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        Book book = entityManager.find(Book.class, 1L);
        Comment comment = entityManager.find(Comment.class, 1L);
        bookCommentIdTemplate = BookCommentId.builder().bookId((long) book.getId()).commentId(comment.getId()).build();
    }

    @Test
    void testEqualsAndHashCode() {
        BookCommentId bookCommentIdTheSame = bookCommentIdTemplate.toBuilder().build();
        BookCommentId bookCommentIdTheSame2 = bookCommentIdTemplate.toBuilder().build();
        BookCommentId bookCommentIdDifferent = bookCommentIdTemplate.toBuilder().bookId(100L).build();

        // Reflexivity
        assertEquals(bookCommentIdTheSame, bookCommentIdTheSame2);

        // Symmetry
        assertEquals(bookCommentIdTheSame, bookCommentIdTheSame2);
        assertEquals(bookCommentIdTheSame2, bookCommentIdTheSame);

        // Transitivity
        assertEquals(bookCommentIdTheSame, bookCommentIdTheSame2);

        // Inequality
        assertEquals(bookCommentIdTheSame, bookCommentIdTheSame2);
        assertNotEquals(bookCommentIdTheSame, null);
        assertNotEquals(bookCommentIdTheSame, new Object());
        assertNotEquals(bookCommentIdTheSame.getBookId(), bookCommentIdDifferent.getBookId());

        // HashCode
        assertEquals(bookCommentIdTheSame.hashCode(), bookCommentIdTheSame2.hashCode());
        assertNotEquals(bookCommentIdTheSame.hashCode(), bookCommentIdDifferent.hashCode());

        // ToString
        assertEquals(bookCommentIdTheSame.toString(), bookCommentIdTheSame2.toString());
    }
}
