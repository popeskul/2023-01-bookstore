package com.otus.bookstore.repository;

import com.otus.bookstore.model.Author;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.model.Genre;
import com.otus.bookstore.repository.impl.AuthorRepositoryJpa;
import com.otus.bookstore.repository.impl.BookRepositoryJpa;
import com.otus.bookstore.repository.impl.GenreRepositoryJpa;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({BookRepositoryJpa.class, AuthorRepositoryJpa.class, GenreRepositoryJpa.class})
class BookRepositoryJpaTest {
    private static final String title = "Book1";
    private static final String title2 = "Book2";
    private static final String description = "Description1";
    private static final String description2 = "Description2";
    private static final BigDecimal price = BigDecimal.valueOf(100.0);
    private static final int bookId = 11;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    Book initialBook;
    Author initialAuthor;
    Genre initialGenre;

    @BeforeEach
    void setUp() {
        initialBook = entityManager.find(Book.class, 1);
        initialAuthor = entityManager.find(Author.class, initialBook.getAuthor().getId());
        initialGenre = entityManager.find(Genre.class, initialBook.getGenre().getId());

        entityManager.clear();
    }

    @Test
    void shouldSaveBook() {
        Book book = Book.builder()
                .title(title)
                .description(description)
                .price(price)
                .author(initialAuthor)
                .genre(initialGenre)
                .build();

        bookRepository.save(book);

        Book actual = entityManager.find(Book.class, book.getId());

        assertThat(actual).isNotNull();
        assertThat(actual.getTitle()).isEqualTo(title);
        assertThat(actual.getId()).isGreaterThan(0);

        assertThat(actual.getAuthor()).isNotNull();
        assertThat(actual.getAuthor().getId()).isEqualTo(initialAuthor.getId());

        assertThat(actual.getGenre()).isNotNull();
        assertThat(actual.getGenre().getId()).isEqualTo(initialGenre.getId());
    }

    @Test
    void shouldUpdateBook() {
        // enable statistics
        SessionFactory sessionFactory = entityManager.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().clear();
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        Book updatedBook = initialBook.toBuilder()
                .title(title2)
                .description(description2)
                .build();

        bookRepository.save(updatedBook).orElseThrow();

        Book updatedActual = entityManager.find(Book.class, updatedBook.getId());

        assertThat(updatedActual).isNotNull();
        assertThat(updatedActual.getTitle()).isEqualTo(title2);
        assertThat(updatedActual.getDescription()).isEqualTo(description2);
        assertThat(updatedActual.getId()).isGreaterThan(0);
        assertThat(updatedActual.getId()).isEqualTo(initialBook.getId());

        assertThat(updatedActual.getAuthor()).isNotNull();
        assertThat(updatedActual.getAuthor().getId()).isEqualTo(initialAuthor.getId());

        assertThat(updatedActual.getGenre()).isNotNull();
        assertThat(updatedActual.getGenre().getId()).isEqualTo(initialGenre.getId());
    }

    @Test
    void shouldFindById() {
        // enable statistics
        SessionFactory sessionFactory = entityManager.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().clear();
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        // when
        Optional<Book> actual = bookRepository.findById(initialBook.getId());

        assertThat(actual).isPresent();
        assertThat(actual.get().getTitle()).isEqualTo(initialBook.getTitle());
        assertThat(actual.get().getId()).isEqualTo(initialBook.getId());

        assertThat(actual.get().getAuthor()).isNotNull();
        assertThat(actual.get().getAuthor().getId()).isEqualTo(initialAuthor.getId());

        assertThat(actual.get().getGenre()).isNotNull();
        assertThat(actual.get().getGenre().getId()).isEqualTo(initialGenre.getId());

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.get().getTitle()).isEqualTo(initialBook.getTitle());
        assertThat(actual.get().getId()).isGreaterThan(0);

        assertThat(actual.get().getAuthor()).isNotNull();
        assertThat(actual.get().getAuthor().getId()).isEqualTo(initialAuthor.getId());

        assertThat(actual.get().getGenre()).isNotNull();
        assertThat(actual.get().getGenre().getId()).isEqualTo(initialGenre.getId());

        // check statistics
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    void shouldFindAllBooks() {
        // enable statistics
        SessionFactory sessionFactory = entityManager.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().clear();
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        System.out.println("entityManager = ");

        List<Book> books = bookRepository.findAll();

        assertThat(books).isNotEmpty();
        assertThat(books).hasSize(4);

        // check statistics
        Arrays.stream(sessionFactory.getStatistics().getQueries()).forEach(System.out::println);
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    void shouldDeleteById() {
        bookRepository.deleteById(initialBook.getId());

        Book actual = entityManager.find(Book.class, initialBook.getId());

        assertThat(actual).isNull();
    }

    @Test
    void shouldDeleteAll() {
        List<Book> before = bookRepository.findAll();

        assertThat(before).isNotEmpty();

        bookRepository.deleteAll();

        List<Book> after = bookRepository.findAll();

        assertThat(after).isEmpty();

        assertThat(after.size()).isEqualTo(0);
    }
}
