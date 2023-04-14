package com.otus.bookstore.model;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataMongoTest
@EnableMongock
@ActiveProfiles("test")
class CommentTest {
    private Author author;
    private Book book;
    private Comment commentTemplate;

    @BeforeEach
    void setUp() {
        author = Author.builder().id("1").name("John Doe").email("2asd@mail.com").build();
        book = Book.builder().id("1").title("Test").author(author).build();
        commentTemplate = Comment.builder().id("10").book(book).text("Test").build();
    }

    @Test
    void testEqualsAndHashCode() {
        Comment commentTheSame = commentTemplate.toBuilder().build();
        Comment commentTheSame2 = commentTemplate.toBuilder().build();
        Comment commentDifferent = commentTemplate.toBuilder().id("31").text("another text").build();

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
}
