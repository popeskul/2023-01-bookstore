package com.otus.bookstore.service;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.*;
import com.otus.bookstore.repository.BookRepository;
import com.otus.bookstore.repository.CommentRepository;
import com.otus.bookstore.repository.impl.CommentRepositoryJpa;
import com.otus.bookstore.service.impl.CommentServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.security.InvalidParameterException;
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

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        author = Author.builder()
                .email("asd@mail.com")
                .name("name1")
                .build();

        book = Book.builder().title("title1").author(author).build();

        comment = Comment.builder().id(0L).book(book).text("text1").build();
    }

    @Test
    void shouldCreateComment() {
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment savedComment = commentService.create(comment);
        assertThat(savedComment).isEqualTo(comment);
    }

    @Test
    void shouldThrowExceptionWhenCreateComment() {
        when(commentRepository.save(comment)).thenThrow(new EntitySaveException(String.format(CommentRepositoryJpa.ERROR_CREATE_COMMENT, comment)));

        assertThatThrownBy(() -> commentService.create(comment))
                .isInstanceOf(EntitySaveException.class)
                .hasMessageContaining(String.format(CommentRepositoryJpa.ERROR_CREATE_COMMENT, comment));
    }

    @Test
    void shouldGetAllComments() {
        when(commentRepository.findAll()).thenReturn(List.of(comment));

        List<Comment> comments = commentService.getAll();
        assertThat(comments).hasSize(1);
        assertThat(comments.get(0)).isEqualTo(comment);
    }

    @Test
    void shouldGetById() {
        when(commentRepository.findById(ID)).thenReturn(java.util.Optional.of(comment));

        Optional<Comment> commentById = commentService.findById(ID);
        assertThat(commentById).isPresent();
        assertThat(commentById.get()).isEqualTo(comment);
    }

    @Test
    @DisplayName("should not find comment by id")
    void shouldThrowExceptionWhenResultGetByIdIsEmpty() {
        when(commentRepository.findById(ID)).thenReturn(Optional.empty());

        Optional<Comment> comment = commentService.findById(ID);

        assertThat(comment).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenDeleteCommentById() {
        doThrow(new InvalidParameterException()).when(commentRepository).deleteById(ID);

        assertThatThrownBy(() -> commentService.deleteById(ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format(CommentServiceImpl.ERROR_NOT_FOUND_AUTHOR, ID));
    }

    @Test
    void shouldDeleteCommentById() {
        when(commentRepository.findById(ID)).thenReturn(Optional.of(comment));

        commentService.deleteById(ID);
    }

    @Test
    void getCommentsByBookIdTest() {
        long bookId = 1L;

        Book preBook = Book.builder().id(bookId).build();

        List<Comment> commentsByBookId = List.of(
                Comment.builder().id(1L).book(book).text("text1").build(),
                Comment.builder().id(2L).book(book).text("text2").build()
        );

        Book book = preBook.toBuilder().comments(commentsByBookId).build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        List<Comment> comments = commentService.findByBookId(bookId);

        assertThat(comments).hasSize(2);
        assertThat(comments).containsAll(commentsByBookId);
    }
}
