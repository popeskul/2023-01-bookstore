package com.otus.bookstore.service;

import com.otus.bookstore.model.*;
import com.otus.bookstore.repository.BookCommentRepository;
import com.otus.bookstore.service.impl.BookCommentServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookCommentServiceImplTest {
    private static final BigDecimal PRICE = BigDecimal.valueOf(19.99);
    private static final long BOOK_ID = 1L;
    private static final long COMMENT_ID = 1L;
    private static final int AUTHOR_ID = 1;
    private static final int GENRE_ID = 1;
    private static final String BOOK_TITLE = "Title";
    private static final String BOOK_DESCRIPTION = "Description";
    private static final String AUTHOR_NAME = "John Smith";
    private static final String AUTHOR_EMAIL = "some@mail.com";
    private static final String GENRE_NAME = "Science Fiction";
    private static final String COMMENT_TEXT = "Text";

    @MockBean
    private BookCommentRepository bookCommentRepository;

    @Autowired
    private BookCommentService bookCommentService;

    private Author author;
    private Genre genre;
    private Book book;
    private Comment comment;
    private BookCommentId bookCommentId;
    private BookComment bookComment;

    @BeforeEach
    void setUp() {
        author = Author.builder().id(AUTHOR_ID).name(AUTHOR_NAME).email(AUTHOR_EMAIL).build();
        genre = Genre.builder().id(GENRE_ID).name(GENRE_NAME).build();
        book = Book.builder().id(BOOK_ID).genre(genre).title(BOOK_TITLE).description(BOOK_DESCRIPTION).price(PRICE).author(author).build();
        comment = Comment.builder().id(COMMENT_ID).book(book).author(author).text(COMMENT_TEXT).build();
        bookCommentId = BookCommentId.builder().bookId(BOOK_ID).commentId(COMMENT_ID).build();
        bookComment = BookComment.builder().id(bookCommentId).book(book).comment(comment).build();
    }

    @Test
    void shouldSaveNewBookComment() {
        when(bookCommentRepository.save(bookComment)).thenReturn(bookComment);

        BookComment result = bookCommentService.save(bookComment);

        assertThat(result).isEqualTo(bookComment);
        verify(bookCommentRepository, times(1)).save(bookComment);
    }

    @Test
    void shouldThrowExceptionWhenSaveNewBookComment() {
        doThrow(new InvalidParameterException()).when(bookCommentRepository).save(bookComment);

        assertThatThrownBy(() -> bookCommentService.save(bookComment))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format(BookCommentServiceImpl.BOOK_COMMENT_WITH_ID_NOT_FOUND, bookComment));
    }

    @Test
    void shouldGetAllBookComments() {
        when(bookCommentRepository.findAll()).thenReturn(List.of(bookComment));

        List<BookComment> result = bookCommentService.getAll();

        assertThat(result).isEqualTo(List.of(bookComment));
        verify(bookCommentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("should get book comment by id")
    void shouldGetById() {
        when(bookCommentRepository.findById(bookCommentId)).thenReturn(Optional.of(bookComment));

        Optional<BookComment> result = bookCommentService.getById(bookCommentId);

        assertThat(result).isEqualTo(Optional.of(bookComment));
        verify(bookCommentRepository, times(1)).findById(bookCommentId);
    }

    @Test
    @DisplayName("should throw exception when try to get book comment by id")
    void shouldThrowExceptionWhenTryToGetById() {
        doThrow(new EntityNotFoundException(String.format(BookCommentServiceImpl.BOOK_COMMENT_WITH_ID_NOT_FOUND, bookCommentId)))
                .when(bookCommentRepository).findById(bookCommentId);

        assertThatThrownBy(() -> bookCommentService.getById(bookCommentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format(BookCommentServiceImpl.BOOK_COMMENT_WITH_ID_NOT_FOUND, bookCommentId));
    }

    @Test
    @DisplayName("should delete book comment by id")
    void shouldDeleteById() {
        when(bookCommentRepository.deleteById(bookCommentId)).thenReturn(true);

        bookCommentService.deleteById(bookCommentId);

        verify(bookCommentRepository, times(1)).deleteById(bookCommentId);
    }

    @Test
    @DisplayName("should throw exception when try to delete book comment by BookCommentId")
    void shouldThrowExceptionWhenTryToDeleteById() {
        doThrow(new EntityNotFoundException(String.format(BookCommentServiceImpl.BOOK_COMMENT_WITH_ID_NOT_FOUND, bookCommentId)))
                .when(bookCommentRepository).deleteById(bookCommentId);

        assertThatThrownBy(() -> bookCommentService.deleteById(bookCommentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format(BookCommentServiceImpl.BOOK_COMMENT_WITH_ID_NOT_FOUND, bookCommentId));
    }
}
