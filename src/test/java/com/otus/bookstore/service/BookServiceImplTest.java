package com.otus.bookstore.service;

import com.otus.bookstore.exception.AuthorNotFoundException;
import com.otus.bookstore.exception.BookBadRequestException;
import com.otus.bookstore.exception.BookErrorSavedException;
import com.otus.bookstore.exception.GenreNotFoundException;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.service.impl.AuthorServiceImpl;
import com.otus.bookstore.service.impl.BookServiceImpl;
import com.otus.bookstore.service.impl.GenreServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import({BookServiceImpl.class, AuthorServiceImpl.class, GenreServiceImpl.class})
public class BookServiceImplTest {
    private static final String title = "Some title";
    private static final String title2 = "Some title2";
    private static final String description = "Some description";
    private static final String description2 = "Some description2";
    private static final BigDecimal price = BigDecimal.valueOf(100);

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private GenreService genreService;

    @Test
    void shouldCreateBook() {
        int genreId = genreService.getAll().get(0).getId();

        assertThat(genreId).isPositive();

        int authorId = authorService.getAll().get(0).getId();

        assertThat(authorId).isPositive();

        // Act
        Optional<Integer> id = bookService.create(title, description, price, genreId, authorId);

        // Assert
        assertThat(id).isPresent();
        assertThat(id.get()).isPositive();

        Optional<Book> actual = bookService.getById(id.get());
        assertThat(actual).isPresent();
        assertThat(actual.get().getTitle()).isEqualTo(title);
        assertThat(actual.get().getDescription()).isEqualTo(description);
        assertThat(actual.get().getPrice().longValue()).isEqualTo(price.longValue());
        assertThat(actual.get().getGenre().getId()).isEqualTo(genreId);
        assertThat(actual.get().getAuthor().getId()).isEqualTo(authorId);
    }

    @Test
    void shouldNotCreateBookWithBadRequest() {
        int genreId = genreService.getAll().get(0).getId();

        assertThat(genreId).isPositive();

        int authorId = authorService.getAll().get(0).getId();

        assertThat(authorId).isPositive();

        // bad request
        assertThatThrownBy(() -> bookService.create(null, null, null, genreId, authorId))
                .isInstanceOf(BookErrorSavedException.class)
                .hasMessageContaining(BookErrorSavedException.ERROR_CREATE_BOOK);
    }

    @Test
    void shouldNotCreateBookWithBadAuthorId() {
        int genreId = genreService.getAll().get(0).getId();

        assertThat(genreId).isPositive();

        // bad author id
        assertThatThrownBy(() -> bookService.create(title, description, price, 0, genreId))
                .isInstanceOf(AuthorNotFoundException.class)
                .hasMessageContaining(AuthorNotFoundException.ERROR_AUTHOR_NOT_FOUND);
    }

    @Test
    void shouldNotCreateBookWithBadGenreId() {
        int authorId = authorService.getAll().get(0).getId();

        assertThat(authorId).isPositive();

        // bad genre id
        assertThatThrownBy(() -> bookService.create(title, description, price, authorId, 0))
                .isInstanceOf(GenreNotFoundException.class)
                .hasMessageContaining(GenreNotFoundException.ERROR_GENRE_NOT_FOUND);
    }

    @Test
    void shouldGetById() {
        int genreId = genreService.getAll().get(0).getId();

        assertThat(genreId).isPositive();

        int authorId = authorService.getAll().get(0).getId();

        assertThat(authorId).isPositive();

        // Act
        Optional<Integer> id = bookService.create(title, description, price, genreId, authorId);

        // Assert
        assertThat(id).isPresent();
        assertThat(id.get()).isPositive();

        Optional<Book> actual = bookService.getById(id.get());
        assertThat(actual).isPresent();
        assertThat(actual.get().getTitle()).isEqualTo(title);
        assertThat(actual.get().getDescription()).isEqualTo(description);
        assertThat(actual.get().getPrice().longValue()).isEqualTo(price.longValue());
        assertThat(actual.get().getGenre().getId()).isEqualTo(genreId);
        assertThat(actual.get().getAuthor().getId()).isEqualTo(authorId);
    }

    @Test
    void shouldGetAll() {
        int genreId = genreService.getAll().get(0).getId();

        assertThat(genreId).isPositive();

        int authorId = authorService.getAll().get(0).getId();

        assertThat(authorId).isPositive();

        // Act
        Optional<Integer> id = bookService.create(title, description, price, genreId, authorId);

        // Assert
        assertThat(id).isPresent();
        assertThat(id.get()).isPositive();

        Optional<Book> actual = bookService.getById(id.get());
        assertThat(actual).isPresent();
        assertThat(actual.get().getTitle()).isEqualTo(title);
        assertThat(actual.get().getDescription()).isEqualTo(description);
        assertThat(actual.get().getPrice().longValue()).isEqualTo(price.longValue());
        assertThat(actual.get().getGenre().getId()).isEqualTo(genreId);
        assertThat(actual.get().getAuthor().getId()).isEqualTo(authorId);
    }

    @Test
    void shouldUpdate() {
        int genreId = genreService.getAll().get(0).getId();

        assertThat(genreId).isPositive();

        int authorId = authorService.getAll().get(0).getId();

        assertThat(authorId).isPositive();

        // Act
        Optional<Integer> id = bookService.create(title, description, price, genreId, authorId);

        // Assert
        assertThat(id).isPresent();
        assertThat(id.get()).isPositive();

        Optional<Book> actual = bookService.getById(id.get());
        assertThat(actual).isPresent();
        assertThat(actual.get().getTitle()).isEqualTo(title);
        assertThat(actual.get().getDescription()).isEqualTo(description);
        assertThat(actual.get().getPrice().longValue()).isEqualTo(price.longValue());
        assertThat(actual.get().getGenre().getId()).isEqualTo(genreId);
        assertThat(actual.get().getAuthor().getId()).isEqualTo(authorId);

        // Act
        bookService.update(id.get(), title2, description2, price, genreId, authorId);

        // Assert
        Optional<Book> actual2 = bookService.getById(id.get());
        assertThat(actual2).isPresent();
        assertThat(actual2.get().getTitle()).isEqualTo(title2);
        assertThat(actual2.get().getDescription()).isEqualTo(description2);
        assertThat(actual2.get().getPrice().longValue()).isEqualTo(price.longValue());
        assertThat(actual2.get().getGenre().getId()).isEqualTo(genreId);
        assertThat(actual2.get().getAuthor().getId()).isEqualTo(authorId);
    }

    @Test
    void shouldNotUpdateWithBadBookId() {
        int genreId = genreService.getAll().get(0).getId();

        assertThat(genreId).isPositive();

        int authorId = authorService.getAll().get(0).getId();

        assertThat(authorId).isPositive();

        // Can't update book with id = 0
        assertThatThrownBy(() -> bookService.update(0, title2, description2, price, authorId, genreId))
                .isInstanceOf(BookBadRequestException.class)
                .hasMessageContaining(BookBadRequestException.ERROR_BAD_REQUEST);
    }

    @Test
    void shouldNotUpdateWithBadName() {
        int genreId = genreService.getAll().get(0).getId();

        assertThat(genreId).isPositive();

        int authorId = authorService.getAll().get(0).getId();

        assertThat(authorId).isPositive();

        // Act
        Optional<Integer> id = bookService.create(title, description, price, genreId, authorId);

        // Assert
        assertThat(id).isPresent();
        assertThat(id.get()).isPositive();

        Optional<Book> actual = bookService.getById(id.get());
        assertThat(actual).isPresent();
        assertThat(actual.get().getTitle()).isEqualTo(title);
        assertThat(actual.get().getDescription()).isEqualTo(description);
        assertThat(actual.get().getPrice().longValue()).isEqualTo(price.longValue());
        assertThat(actual.get().getGenre().getId()).isEqualTo(genreId);
        assertThat(actual.get().getAuthor().getId()).isEqualTo(authorId);

        // bad request
        assertThatThrownBy(() -> bookService.update(id.get(), null, null, null, genreId, authorId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldDeleteById() {
        int genreId = genreService.getAll().get(0).getId();

        assertThat(genreId).isPositive();

        int authorId = authorService.getAll().get(0).getId();

        assertThat(authorId).isPositive();

        // Act
        Optional<Integer> id = bookService.create(title, description, price, genreId, authorId);

        // Assert
        assertThat(id).isPresent();
        assertThat(id.get()).isPositive();

        Optional<Book> actual = bookService.getById(id.get());
        assertThat(actual).isPresent();
        assertThat(actual.get().getTitle()).isEqualTo(title);
        assertThat(actual.get().getDescription()).isEqualTo(description);
        assertThat(actual.get().getPrice().longValue()).isEqualTo(price.longValue());
        assertThat(actual.get().getGenre().getId()).isEqualTo(genreId);
        assertThat(actual.get().getAuthor().getId()).isEqualTo(authorId);

        // Act
        bookService.deleteById(id.get());

        // Assert
        Optional<Book> actual2 = bookService.getById(id.get());
        assertThat(actual2).isEmpty();
    }
}
