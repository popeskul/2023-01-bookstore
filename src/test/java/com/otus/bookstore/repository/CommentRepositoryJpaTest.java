package com.otus.bookstore.repository;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Author;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.model.Comment;
import com.otus.bookstore.repository.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;

@DataJpaTest
@Import({CommentRepositoryJpa.class, BookRepositoryJpa.class})
public class CommentRepositoryJpaTest {
    @Autowired
    private CommentRepository commentRepository;

    @Mock
    private CommentRepository commentRepositoryMock;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final String commentText = "Some comment text";

    private Book book;
    private Author author;
    private Comment comment;

    @BeforeEach
    void setUp() {
        book = bookRepository.findById(1).orElseThrow();
        author = book.getAuthor();

        comment = Comment.builder()
                .book(book)
                .author(author)
                .text(commentText)
                .build();
    }

    @Test
    public void shouldSaveComment() {
        Comment newComment = comment.toBuilder().build();

        try {
            commentRepository.save(newComment);
        } catch (EntitySaveException e) {
            assertThatThrownBy(() -> commentRepository.save(newComment))
                    .isInstanceOf(EntitySaveException.class)
                    .hasMessageContaining(String.format(CommentRepositoryJpa.ERROR_WHILE_SAVING_COMMENT, newComment));
        }

        Comment actual = entityManager.find(Comment.class, newComment.getId());

        assertThat(actual).isNotNull();
        assertThat(actual.getText()).isEqualTo(commentText);
        assertThat(actual.getId()).isGreaterThan(0);
        assertThat(actual.getBook()).isEqualTo(newComment.getBook());
        assertThat(actual.getAuthor()).isEqualTo(newComment.getAuthor());
    }

    @Test
    public void shouldThrowCommentErrorSavedExceptionWhenSaveComment() {
        doThrow(new EntitySaveException(String.format(CommentRepositoryJpa.ERROR_WHILE_SAVING_COMMENT, comment)))
                .when(commentRepositoryMock).save(comment);

        assertThatThrownBy(() -> commentRepositoryMock.save(comment))
                .isInstanceOf(EntitySaveException.class)
                .hasMessageContaining(String.format(CommentRepositoryJpa.ERROR_WHILE_SAVING_COMMENT, comment));
    }

    @Test
    public void shouldThrowCommentErrorSavedExceptionOnRuntimeException() {
        doThrow(new EntitySaveException(String.format(CommentRepositoryJpa.ERROR_WHILE_SAVING_COMMENT, comment)))
                .when(commentRepositoryMock).save(comment);

        assertThatThrownBy(() -> commentRepositoryMock.save(comment))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void shouldFindById() {
        Comment savedComment = commentRepository.save(comment);

        Optional<Comment> actual = commentRepository.findById(savedComment.getId());

        assertThat(actual).isPresent();
        assertThat(actual.get().getText()).isEqualTo(commentText);
        assertThat(actual.get().getId()).isEqualTo(savedComment.getId());
        assertThat(actual.get().getBook()).isEqualTo(comment.getBook());
        assertThat(actual.get().getAuthor()).isEqualTo(comment.getAuthor());
    }

    @Test
    public void shouldFindAllComments() {
        Comment comment1 = comment.toBuilder().id(0L).text("asd").build();
        Comment comment2 = comment.toBuilder().id(0L).text("qwe").build();

        List<Comment> comments = Arrays.asList(comment1, comment2);
        comments.forEach(entityManager::persist);

        List<Comment> actual = commentRepository.findAll();

        assertThat(actual).isNotNull();
        assertThat(actual).contains(comment1, comment2);
    }

    @Test
    public void shouldDeleteById() {
        Comment savedComment = entityManager.persist(comment);

        commentRepository.deleteById(savedComment.getId());

        Comment actual = entityManager.find(Comment.class, comment.getId());

        assertThat(actual).isNull();
    }
}
