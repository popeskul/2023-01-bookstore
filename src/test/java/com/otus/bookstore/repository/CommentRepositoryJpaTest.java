package com.otus.bookstore.repository;

import com.github.cloudyrock.spring.v5.EnableMongock;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;

@DataMongoTest
@ActiveProfiles("test")
@EnableMongock
@ComponentScan({
        "com.otus.bookstore.repository", "com.otus.bookstore.model"
})
public class CommentRepositoryJpaTest {
    @Autowired
    private CommentRepository commentRepository;

    @Mock
    private CommentRepository commentRepositoryMock;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final String commentText = "Some comment text";

    private Book book;
    private Comment comment;

    @BeforeEach
    void setUp() {
        var books = bookRepository.findAll();
        assertThat(books).isNotNull();
        assertThat(books.size()).isGreaterThan(0);

        book = bookRepository.findById(books.get(0).getId()).orElseThrow();

        comment = Comment.builder()
                .book(book)
                .text(commentText)
                .build();
    }

    @Test
    public void shouldSaveComment() {
        Comment newComment = comment.toBuilder().build();

        Comment savedComment = commentRepository.save(newComment);

        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getId().length()).isGreaterThan(0);
        assertThat(savedComment.getText()).isEqualTo(commentText);
        assertThat(savedComment.getBook()).isEqualTo(book);

        Comment actual = commentRepository.findById(savedComment.getId()).orElseThrow();
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(savedComment.getId());
        assertThat(actual.getText()).isEqualTo(commentText);
        assertThat(actual.getBook()).isEqualTo(book);

        assertThat(actual).isEqualTo(savedComment);
    }

    @Test
    public void shouldThrowCommentErrorSavedExceptionWhenSaveComment() {
        doThrow(DataIntegrityViolationException.class).when(commentRepositoryMock).save(comment);

        assertThatThrownBy(() -> commentRepositoryMock.save(comment))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void shouldThrowCommentErrorSavedExceptionOnRuntimeException() {
        doThrow(DataIntegrityViolationException.class).when(commentRepositoryMock).save(comment);

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
    }

    @Test
    public void shouldFindAllComments() {
        mongoTemplate.dropCollection(Comment.class);

        Comment comment1 = comment.toBuilder().text("asd").book(book).build();
        Comment comment2 = comment.toBuilder().text("qwe").book(book).build();

        List<Comment> comments = Arrays.asList(comment1, comment2);
        mongoTemplate.insertAll(comments);

        List<Comment> actual = commentRepository.findAll();

        // add id to comments
        Comment comment1WithData = comment1.toBuilder().id(actual.get(0).getId()).createdAt(actual.get(0).getCreatedAt()).build();
        Comment comment2WithData = comment2.toBuilder().id(actual.get(1).getId()).createdAt(actual.get(1).getCreatedAt()).build();

        assertThat(actual).isNotNull();
        assertThat(actual).contains(comment1WithData, comment2WithData);
    }

    @Test
    public void shouldDeleteById() {
        Comment savedComment = commentRepository.save(comment);

        commentRepository.deleteById(savedComment.getId());

        Comment actual = commentRepository.findById(savedComment.getId()).orElse(null);

        assertThat(actual).isNull();
    }
}
