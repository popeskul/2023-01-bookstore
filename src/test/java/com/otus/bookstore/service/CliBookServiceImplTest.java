package com.otus.bookstore.service;

import com.github.cloudyrock.spring.v5.EnableMongock;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.model.Genre;
import com.otus.bookstore.service.impl.CliBookServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
@EnableMongock
@ActiveProfiles("test")
@ComponentScan({
        "com.otus.bookstore.service", "com.otus.bookstore.repository",
        "com.otus.bookstore.model", "com.otus.bookstore.validator"
})
@Import({CliBookServiceImpl.class})
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
        var genres = genreService.getAll();
        assertThat(genres).isNotEmpty();

        String genreId = genres.get(0).getId();

        assertThat(genreId).isNotNull();

        var authors = authorService.getAll();
        assertThat(authors).isNotEmpty();

        String authorId = authors.get(0).getId();
        assertThat(authorId).isNotNull();
        assertThat(authorId.length()).isGreaterThan(0);

        // Act
        Book book = cliBookService.create(title, description, price, authorId, genreId);

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
        assertThat(genre.getId().length()).isGreaterThan(0);

        var authors = authorService.getAll();
        assertThat(authors).isNotEmpty();

        var author = authors.get(0);

        assertThat(author).isNotNull();
        assertThat(author.getId().length()).isGreaterThan(0);

        Book badBook = Book.builder().id("").genre(genre).author(author).build();

        // bad request
        assertThatThrownBy(() -> cliBookService.create(badBook.getTitle(), badBook.getDescription(),
                badBook.getPrice(), badBook.getAuthor().getId(), badBook.getGenre().getId()))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldNotCreateBookWithBadAuthorId() {
        var genreId = "1";
        var badAuthorId = "0";

        assertThat(genreId.length()).isGreaterThan(0);

        // bad author id
        assertThatThrownBy(() -> cliBookService.create(title, description, price, badAuthorId, genreId))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void shouldNotCreateBookWithBadGenreId() {
        var authors = authorService.getAll();
        assertThat(authors).isNotEmpty();

        var authorId = authors.get(0).getId();
        assertThat(authorId).isNotNull();

        // bad genre id
        assertThatThrownBy(() -> cliBookService.create(title, description, price, authorId, "0"))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void shouldGetById() {
        var genres = genreService.getAll();
        assertThat(genres).isNotEmpty();

        var genreId = genres.get(0).getId();
        assertThat(genreId).isNotNull();

        var authors = authorService.getAll();
        assertThat(authors).isNotEmpty();

        var authorId = authors.get(0).getId();
        assertThat(authorId).isNotNull();

        // Act
        Book book = cliBookService.create(title, description, price, authorId, genreId);

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
        var genres = genreService.getAll();
        assertThat(genres).isNotEmpty();

        var genreId = genres.get(0).getId();
        assertThat(genreId).isNotNull();

        var authors = authorService.getAll();
        assertThat(authors).isNotEmpty();

        var authorId = authors.get(0).getId();
        assertThat(authorId.length()).isGreaterThan(0);

        // Act
        Book book = cliBookService.create(title, description, price, authorId, genreId);

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
        cliBookService.update(book.getId(), title2, description2, price, authorId, genreId);

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
        // get first book
        var books = cliBookService.getAll();
        assertThat(books).isNotEmpty();

        var bookId = books.get(0).getId();
        assertThat(bookId.length()).isGreaterThan(0);

        var genres = genreService.getAll();
        assertThat(genres).isNotEmpty();

        String genreId = genres.get(0).getId();

        assertThat(genreId.length()).isGreaterThan(0);

        var authors = authorService.getAll();
        assertThat(authors).isNotEmpty();

        String authorId = authors.get(0).getId();
        assertThat(authorId.length()).isGreaterThan(0);

        // Can't update book with id = 0
        assertThatThrownBy(() -> cliBookService.update(bookId, null, null, price, authorId, genreId))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldNotUpdateWithBadName() {
        var genres = genreService.getAll();
        assertThat(genres).isNotEmpty();

        String genreId = genres.get(0).getId();
        assertThat(genreId.length()).isGreaterThan(0);

        var authors = authorService.getAll();
        assertThat(authors).isNotEmpty();

        String authorId = authors.get(0).getId();
        assertThat(authorId.length()).isGreaterThan(0);

        // Act
        Book book = cliBookService.create(title, description, price, authorId, genreId);

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
        assertThatThrownBy(() -> cliBookService.update(book.getId(), null, null, null, authorId, genreId))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldDeleteById() {
        var genres = genreService.getAll();
        assertThat(genres).isNotEmpty();

        String genreId = genres.get(0).getId();
        assertThat(genreId.length()).isGreaterThan(0);

        var authors = authorService.getAll();
        assertThat(authors).isNotEmpty();

        String authorId = authors.get(0).getId();
        assertThat(authorId.length()).isGreaterThan(0);

        // Act
        Book book = cliBookService.create(title, description, price, authorId, genreId);

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
