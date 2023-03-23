package com.otus.bookstore.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
class CommentTest {
    private Author author;
    private Book book;
    private Comment commentTemplate;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        book = entityManager.find(Book.class, 1L);
        author = book.getAuthor();

        commentTemplate = Comment.builder().id(10L).book(book).author(author).text("Test").build();
    }

    @Test
    void testInitialSavedData() {
        List<Comment> allComments = entityManager.getEntityManager()
                .createQuery("select c from Comment c", Comment.class)
                .getResultList();

        assertEquals(2, allComments.size());
    }

    @Test
    void testEqualsAndHashCode() {
        Comment commentTheSame = commentTemplate.toBuilder().build();
        Comment commentTheSame2 = commentTemplate.toBuilder().build();
        Comment commentDifferent = commentTemplate.toBuilder().id(31L).text("another text").build();

        // Reflexivity
        assertEquals(commentTheSame, commentTheSame2);

        // Symmetry
        assertEquals(commentTheSame, commentTheSame2);
        assertEquals(commentTheSame2, commentTheSame);

        // Transitivity
        assertEquals(commentTheSame, commentTheSame2);

        // Inequality
        assertEquals(commentTheSame, commentTheSame2);
        assertNotEquals(commentTheSame, null);
        assertNotEquals(commentTheSame, new Object());
        assertNotEquals(commentTheSame.getText(), commentDifferent.getText());

        // HashCode
        assertEquals(commentTheSame.hashCode(), commentTheSame2.hashCode());
        assertNotEquals(commentTheSame.hashCode(), commentDifferent.hashCode());

        // ToString
        assertEquals(commentTheSame.toString(), commentTheSame2.toString());
    }

    @Test
    @DisplayName("create a new comment with correct book and author")
    void shouldCreateCommentWith() {
        Comment newComment = commentTemplate.toBuilder().id(0L).build();

        entityManager.persist(newComment);
        entityManager.flush();

        Comment foundComment = entityManager.find(Comment.class, newComment.getId());

        assertEquals(newComment, foundComment);

        assertEquals(newComment.getBook(), foundComment.getBook());
        assertEquals(newComment.getAuthor(), foundComment.getAuthor());
    }
}
