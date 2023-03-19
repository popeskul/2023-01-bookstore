package com.otus.bookstore.repository;

import com.otus.bookstore.exception.BookCommentErrorSavedException;
import com.otus.bookstore.model.*;
import com.otus.bookstore.repository.impl.BookCommentRepositoryJpa;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({BookCommentRepositoryJpa.class})
class BookCommentRepositoryJpaTest {
    private static final long BOOK_ID = 1L;
    private static final long COMMENT_ID = 1L;

    @Autowired
    private BookCommentRepositoryJpa bookCommentRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Book book;
    private Comment comment;
    private BookComment bookComment;
    private BookCommentId bookCommentId;
    private Author author;

    @BeforeEach
    void setUp() {
        bookComment = bookCommentRepository.findAll().get(0);
        bookCommentId = bookComment.getId();
        book = bookComment.getBook();
        comment = bookComment.getComment();
        author = book.getAuthor();

        assertThat(book).isNotNull();
        assertThat(comment).isNotNull();
        assertThat(bookComment).isNotNull();
        assertThat(bookCommentId).isNotNull();

        assertThat(book.getId()).isEqualTo(BOOK_ID);
        assertThat(comment.getId()).isEqualTo(COMMENT_ID);

        assertThat(bookCommentId.getBookId()).isEqualTo(BOOK_ID);
        assertThat(bookCommentId.getCommentId()).isEqualTo(COMMENT_ID);

        assertThat(bookComment.getBook()).isEqualTo(book);
        assertThat(bookComment.getComment()).isEqualTo(comment);

        assertThat(bookComment.getId()).isEqualTo(bookCommentId);

        assertThat(book.getComments()).contains(bookComment.getComment());
    }

    @Test
    void shouldSaveBookComment() throws BookCommentErrorSavedException {
        // create new comment
        Comment newComment = comment.toBuilder().id(0L).build();

        // save new comment
        Comment actualComment = entityManager.persist(newComment);

        assertThat(actualComment).isNotNull();
        assertThat(actualComment.getId()).isEqualTo(newComment.getId());
        assertThat(actualComment.getText()).isEqualTo(newComment.getText());

        // create new book comment
        BookComment newBookComment = BookComment.builder()
                .id(new BookCommentId(book.getId(), actualComment.getId()))
                .book(book)
                .comment(actualComment)
                .build();

        // save new book comment
        BookComment actualBookComment = bookCommentRepository.save(newBookComment);

        assertThat(actualBookComment).isNotNull();
        assertThat(actualBookComment.getId()).isEqualTo(newBookComment.getId());
        assertThat(actualBookComment.getBook()).isEqualTo(newBookComment.getBook());

        // check comment
        Comment actualComment2 = actualBookComment.getComment();
        assertThat(actualComment2).isNotNull();
        assertThat(actualComment2.getId()).isEqualTo(actualComment.getId());
        assertThat(actualComment2.getText()).isEqualTo(actualComment.getText());
    }

    @Test
    void shouldFindById() {
        // enable statistics
        SessionFactory sessionFactory = entityManager.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        // prepare
        BookComment findBookComment = bookCommentRepository.findAll().get(0);

        // reset statistics
        sessionFactory.getStatistics().clear();

        // get id
        BookCommentId findBookCommentId = findBookComment.getId();

        // find by id
        Optional<BookComment> actual = bookCommentRepository.findById(findBookCommentId);

        assertThat(findBookComment).isNotNull();
        assertThat(findBookComment.getId()).isEqualTo(bookCommentId);

        assertThat(findBookComment.getBook()).isEqualTo(book);
        assertThat(findBookComment.getComment()).isEqualTo(comment);

        assertThat(findBookComment.getBook().getComments()).contains(findBookComment.getComment());

        assertThat(findBookComment.getBook().getAuthor()).isEqualTo(author);

        // check actual
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(findBookComment);

        assertThat(actual.get().getBook()).isEqualTo(book);
        assertThat(actual.get().getComment()).isEqualTo(comment);

        assertThat(actual.get().getBook().getComments()).contains(actual.get().getComment());

        assertThat(actual.get().getBook().getAuthor()).isEqualTo(author);

        // check statistics query
        assertThat(sessionFactory.getStatistics().getQueryExecutionCount()).isEqualTo(1);
    }

    @Test
    void shouldFindAll() {
        // enable statistics
        SessionFactory sessionFactory = entityManager.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        // prepare
        BookComment bookComment1 = bookCommentRepository.findAll().get(0);
        BookComment bookComment2 = bookCommentRepository.findAll().get(1);

        assertThat(bookComment1).isNotNull();
        assertThat(bookComment2).isNotNull();

        assertThat(bookComment1.getId()).isNotNull();
        assertThat(bookComment2.getId()).isNotNull();

        assertThat(bookComment1.getBook()).isNotNull();
        assertThat(bookComment2.getBook()).isNotNull();

        assertThat(bookComment1.getComment()).isNotNull();
        assertThat(bookComment2.getComment()).isNotNull();

        // reset statistics
        sessionFactory.getStatistics().clear();

        List<BookComment> bookCommentsList = bookCommentRepository.findAll();
        assertThat(bookCommentsList).isNotNull();

        assertThat(bookCommentsList.contains(bookComment1)).isTrue();
        assertThat(bookCommentsList.contains(bookComment2)).isTrue();

        // check statistics query
        assertThat(sessionFactory.getStatistics().getQueryExecutionCount()).isEqualTo(1);
    }

    @Test
    void shouldDelete() {
        // prepare
        BookComment findBookComment = bookCommentRepository.findAll().get(0);

        assertThat(findBookComment).isNotNull();
        assertThat(findBookComment.getId()).isEqualTo(bookCommentId);
        assertThat(findBookComment.getBook()).isEqualTo(book);

        // delete
        bookCommentRepository.deleteByBookCommentId(findBookComment.getId());
        entityManager.flush();

        // check
        BookComment actual = entityManager.find(BookComment.class, bookCommentId);
        assertThat(actual).isNull();

        // check book
        Book actualBook = entityManager.find(Book.class, book.getId());
        assertThat(actualBook).isNotNull();
    }

    @Test()
    @DisplayName("should delete book comment by book comment object and don't delete comment and book")
    void shouldDeleteAll() {
        // prepare
        List<BookComment> bookComments = bookCommentRepository.findAll();

        assertThat(bookComments).isNotNull();
        assertThat(bookComments.size()).isEqualTo(2);

        // delete
        entityManager.getEntityManager().createQuery("delete from BookComment").executeUpdate();
        entityManager.flush();

        // check
        List<BookComment> actualBookComments = bookCommentRepository.findAll();
        assertThat(actualBookComments).isEmpty();

        bookComments.forEach(bookComment -> {
            BookComment actualBookComment = entityManager.find(BookComment.class, bookComment.getId());
            assertThat(actualBookComment).isNotNull();

            Comment actualComment = entityManager.find(Comment.class, bookComment.getComment().getId());
            assertThat(actualComment).isNotNull();
        });
    }

    @Test
    void shouldFindAllBookComments() {
        // enable statistics
        SessionFactory sessionFactory = entityManager.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear(); // reset statistics

        List<BookComment> bookComments = bookCommentRepository.findAll();
        assertThat(bookComments).isNotEmpty();
        assertThat(bookComments).hasSize(2);

        // reset statistics
        assertThat(sessionFactory.getStatistics().getQueryExecutionCount()).isEqualTo(1);
    }

    @Test
    void shouldDeleteBookComment() {
        BookCommentId id = new BookCommentId(1L, 1L);

        bookCommentRepository.deleteByBookCommentId(id);

        BookComment bookComment = entityManager.find(BookComment.class, id);

        assertThat(bookComment).isNull();
    }
}
