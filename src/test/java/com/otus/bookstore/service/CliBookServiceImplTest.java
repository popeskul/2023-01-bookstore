package com.otus.bookstore.service;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Author;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.model.Genre;
import com.otus.bookstore.service.impl.AuthorServiceImpl;
import com.otus.bookstore.service.impl.CliBookServiceImpl;
import com.otus.bookstore.service.impl.GenreServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import({CliBookServiceImpl.class, AuthorServiceImpl.class, GenreServiceImpl.class})
public class CliBookServiceImplTest {
    private static final String title = "Some title";
    private static final String title2 = "Some title2";
    private static final String description = "Some description";
    private static final String description2 = "Some description2";
    private static final BigDecimal price = BigDecimal.valueOf(100);

    @Autowired
    private CliBookService cliBookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private GenreService genreService;

    @Test
    void shouldCreateBook() {
        long genreId = genreService.getAll().get(0).getId();

        assertThat(genreId).isPositive();

        long authorId = authorService.getAll().get(0).getId();

        assertThat(authorId).isPositive();

        // Act
        Book book = cliBookService.create(title, description, price, genreId, authorId);

        // Assert
        assertThat(book).isNotNull();

        Optional<Book> actual = cliBookService.getById(book.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get().getTitle()).isEqualTo(title);
        assertThat(actual.get().getDescription()).isEqualTo(description);
        assertThat(actual.get().getPrice().longValue()).isEqualTo(price.longValue());
        assertThat(actual.get().getGenre().getId()).isEqualTo(genreId);
        assertThat(actual.get().getAuthor().getId()).isEqualTo(authorId);
    }

    @Test
    void shouldNotCreateBookWithBadRequest() {
        Genre genre = genreService.getAll().get(0);

        assertThat(genre).isNotNull();
        assertThat(genre.getId()).isPositive();

        Author author = authorService.getAll().get(0);

        assertThat(author).isNotNull();
        assertThat(author.getId()).isPositive();

        Book badBook = Book.builder().id(0L).genre(genre).author(author).build();

        // bad request
        assertThatThrownBy(() -> cliBookService.create(badBook.getTitle(), badBook.getDescription(),
                badBook.getPrice(), badBook.getGenre().getId(), badBook.getAuthor().getId()))
                .isInstanceOf(EntitySaveException.class)
                .hasMessageContaining(new EntitySaveException(badBook).getMessage());
    }

    @Test
    void shouldNotCreateBookWithBadAuthorId() {
        int genreId = 1;
        int badAuthorId = 0;

        assertThat(genreId).isPositive();

        // bad author id
        assertThatThrownBy(() -> cliBookService.create(title, description, price, badAuthorId, genreId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format(AuthorServiceImpl.ERROR_AUTHOR_NOT_FOUND, badAuthorId));
    }

    @Test
    void shouldNotCreateBookWithBadGenreId() {
        long authorId = authorService.getAll().get(0).getId();

        assertThat(authorId).isPositive();

        // bad genre id
        assertThatThrownBy(() -> cliBookService.create(title, description, price, authorId, 0))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessageContaining(GenreServiceImpl.ERROR_ILLEGAL_ARGUMENT);
    }

    @Test
    void shouldGetById() {
        long genreId = genreService.getAll().get(0).getId();

        assertThat(genreId).isPositive();

        long authorId = authorService.getAll().get(0).getId();

        assertThat(authorId).isPositive();

        // Act
        Book book = cliBookService.create(title, description, price, genreId, authorId);

        // Assert
        assertThat(book).isNotNull();

        Optional<Book> actual = cliBookService.getById(book.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get().getTitle()).isEqualTo(title);
        assertThat(actual.get().getDescription()).isEqualTo(description);
        assertThat(actual.get().getPrice().longValue()).isEqualTo(price.longValue());
        assertThat(actual.get().getGenre().getId()).isEqualTo(genreId);
        assertThat(actual.get().getAuthor().getId()).isEqualTo(authorId);
    }

    @Test
    void shouldGetAll() {
        List<Book> actual = cliBookService.getAll();
        assertThat(actual).isNotNull();
        assertThat(actual).isNotEmpty();
    }

    @Test
    void shouldUpdate() {
        long genreId = genreService.getAll().get(0).getId();

        assertThat(genreId).isPositive();

        long authorId = authorService.getAll().get(0).getId();

        assertThat(authorId).isPositive();

        // Act
        Book book = cliBookService.create(title, description, price, genreId, authorId);

        // Assert
        assertThat(book).isNotNull();

        Optional<Book> actual = cliBookService.getById(book.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get().getTitle()).isEqualTo(title);
        assertThat(actual.get().getDescription()).isEqualTo(description);
        assertThat(actual.get().getPrice().longValue()).isEqualTo(price.longValue());
        assertThat(actual.get().getGenre().getId()).isEqualTo(genreId);
        assertThat(actual.get().getAuthor().getId()).isEqualTo(authorId);

        // Act
        cliBookService.update(book.getId(), title2, description2, price, genreId, authorId);

        // Assert
        Optional<Book> actual2 = cliBookService.getById(book.getId());
        assertThat(actual2).isPresent();
        assertThat(actual2.get().getTitle()).isEqualTo(title2);
        assertThat(actual2.get().getDescription()).isEqualTo(description2);
        assertThat(actual2.get().getPrice().longValue()).isEqualTo(price.longValue());
        assertThat(actual2.get().getGenre().getId()).isEqualTo(genreId);
        assertThat(actual2.get().getAuthor().getId()).isEqualTo(authorId);
    }

    @Test
    void shouldNotUpdateWithBadBookId() {
        long genreId = genreService.getAll().get(0).getId();

        assertThat(genreId).isPositive();

        long authorId = authorService.getAll().get(0).getId();

        assertThat(authorId).isPositive();

        // Can't update book with id = 0
        assertThatThrownBy(() -> cliBookService.update(0, title2, description2, price, authorId, genreId))
                .isInstanceOf(InvalidParameterException.class);
    }

    @Test
    void shouldNotUpdateWithBadName() {
        long genreId = genreService.getAll().get(0).getId();

        assertThat(genreId).isPositive();

        long authorId = authorService.getAll().get(0).getId();

        assertThat(authorId).isPositive();

        // Act
        Book book = cliBookService.create(title, description, price, genreId, authorId);

        // Assert
        assertThat(book).isNotNull();

        Optional<Book> actual = cliBookService.getById(book.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get().getTitle()).isEqualTo(title);
        assertThat(actual.get().getDescription()).isEqualTo(description);
        assertThat(actual.get().getPrice().longValue()).isEqualTo(price.longValue());
        assertThat(actual.get().getGenre().getId()).isEqualTo(genreId);
        assertThat(actual.get().getAuthor().getId()).isEqualTo(authorId);

        // bad request
        assertThatThrownBy(() -> cliBookService.update(book.getId(), null, null, null, genreId, authorId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldDeleteById() {
        long genreId = genreService.getAll().get(0).getId();

        assertThat(genreId).isPositive();

        long authorId = authorService.getAll().get(0).getId();

        assertThat(authorId).isPositive();

        // Act
        Book book = cliBookService.create(title, description, price, genreId, authorId);

        // Assert
        assertThat(book).isNotNull();

        Optional<Book> actual = cliBookService.getById(book.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get().getTitle()).isEqualTo(title);
        assertThat(actual.get().getDescription()).isEqualTo(description);
        assertThat(actual.get().getPrice().longValue()).isEqualTo(price.longValue());
        assertThat(actual.get().getGenre().getId()).isEqualTo(genreId);
        assertThat(actual.get().getAuthor().getId()).isEqualTo(authorId);

        // Act
        cliBookService.deleteById(book.getId());

        // Assert
        Optional<Book> actual2 = cliBookService.getById(book.getId());
        assertThat(actual2).isEmpty();
    }
}
