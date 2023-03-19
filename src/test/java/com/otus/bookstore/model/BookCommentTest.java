package com.otus.bookstore.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookCommentTest {
    private final long bookId = 1L;
    private final String bookTitle = "Book 1";
    private final String bookDescription = "Description of book 1";
    private final BigDecimal bookPrice = BigDecimal.valueOf(10);

    private final Long commentId = 2L;
    private final String commentText = "This is a comment";

    @Test
    void testEqualsAndHashCode() {
        BookCommentId bookCommentId = BookCommentId.builder()
                .bookId(bookId)
                .commentId(commentId)
                .build();
        Book book = Book.builder()
                .id((int) bookId)
                .title(bookTitle)
                .description(bookDescription)
                .price(bookPrice)
                .build();
        Comment comment = Comment.builder()
                .id(commentId + 1)
                .text(commentText)
                .build();
        BookComment bookComment1 = BookComment.builder()
                .id(bookCommentId)
                .book(book)
                .comment(comment)
                .build();
        BookComment bookComment2 = BookComment.builder()
                .id(bookCommentId)
                .book(book)
                .comment(comment)
                .build();
        assertEquals(bookComment1, bookComment2);
        assertEquals(bookComment1.hashCode(), bookComment2.hashCode());
    }

    @Test
    void testToString() {
        BookCommentId bookCommentId = BookCommentId.builder()
                .bookId(bookId)
                .commentId(commentId)
                .build();
        Book book = Book.builder()
                .id((int) bookId)
                .title(bookTitle)
                .description(bookDescription)
                .price(bookPrice)
                .build();
        Comment comment = Comment.builder()
                .id(commentId)
                .text(commentText)
                .build();
        BookComment bookComment = BookComment.builder()
                .id(bookCommentId)
                .book(book)
                .comment(comment)
                .build();
        String expected = "BookComment(id=BookCommentId(bookId=1, commentId=2))";
        assertEquals(expected, bookComment.toString());
    }
}
