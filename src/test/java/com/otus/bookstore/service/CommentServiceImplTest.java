package com.otus.bookstore.service;

import com.otus.bookstore.exception.CommentErrorSavedException;
import com.otus.bookstore.exception.CommentNotFoundException;
import com.otus.bookstore.model.*;
import com.otus.bookstore.repository.BookCommentRepository;
import com.otus.bookstore.repository.CommentRepository;
import com.otus.bookstore.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import({CommentServiceImpl.class})
public class CommentServiceImplTest {
    private final long ID = 100L;

    private Author author;
    private Book book;
    private Comment comment;
    private BookComment bookComment;

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @MockBean
    private BookCommentRepository bookCommentRepository;

    @BeforeEach
    public void setUp() {
        author = Author.builder()
                .email("asd@mail.com")
                .name("name1")
                .build();

        book = Book.builder().title("title1").author(author).build();

        comment = Comment.builder().book(book).text("text1").build();

        bookComment = BookComment.builder()
                .id(BookCommentId.builder().bookId(book.getId()).commentId(comment.getId()).build())
                .book(book)
                .comment(comment)
                .build();
    }

    @Test
    void shouldSaveComment() throws CommentErrorSavedException {
        when(commentRepository.save(comment)).thenReturn(comment);
        when(bookCommentRepository.save(bookComment)).thenReturn(bookComment);

        Comment savedComment = commentService.save(comment);
        assertThat(savedComment).isEqualTo(comment);
    }

    @Test
    void shouldThrowExceptionWhenSaveComment() {
        when(commentRepository.save(comment)).thenThrow(new CommentErrorSavedException());

        assertThatThrownBy(() -> commentService.save(comment))
                .isInstanceOf(CommentErrorSavedException.class)
                .hasMessageContaining(CommentErrorSavedException.ERROR_SAVED);
    }

    @Test
    void shouldGetAllComments() {
        when(commentRepository.findAll()).thenReturn(List.of(comment));

        List<Comment> comments = commentService.getAll();
        assertThat(comments).hasSize(1);
        assertThat(comments.get(0)).isEqualTo(comment);
    }

    @Test
    void shouldGetById() throws CommentNotFoundException {
        when(commentRepository.findById(ID)).thenReturn(java.util.Optional.of(comment));

        Optional<Comment> commentById = commentService.findById(ID);
        assertThat(commentById).isPresent();
        assertThat(commentById.get()).isEqualTo(comment);
    }

    @Test
    @DisplayName("should throw CommentNotFoundException result of get comment by id is empty")
    void shouldThrowExceptionWhenResultGetByIdIsEmpty() {
        when(commentRepository.findById(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.findById(ID))
                .isInstanceOf(CommentNotFoundException.class)
                .hasMessageContaining(String.format(CommentNotFoundException.ERROR_NOT_FOUND, ID));
    }

    @Test
    void shouldThrowExceptionWhenDeleteCommentById() {
        doThrow(new IllegalArgumentException()).when(commentRepository).deleteById(ID);

        assertThatThrownBy(() -> commentService.deleteById(ID))
                .isInstanceOf(CommentNotFoundException.class)
                .hasMessageContaining(String.format(CommentNotFoundException.ERROR_NOT_FOUND, ID));
    }

    @Test
    void shouldDeleteCommentById() throws CommentNotFoundException {
        when(commentRepository.findById(ID)).thenReturn(Optional.of(comment));

        commentService.deleteById(ID);
    }
}
