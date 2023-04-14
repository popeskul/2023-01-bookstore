package com.otus.bookstore.repository;

import com.github.cloudyrock.spring.v5.EnableMongock;
import com.otus.bookstore.model.Author;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.model.Comment;
import com.otus.bookstore.model.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@EnableMongock
@ActiveProfiles("test")
@ComponentScan({"com.otus.bookstore.repository", "com.otus.bookstore.model"})
class BookRepositoryJpaTest {
    private static final String title = "Book1";
    private static final String title2 = "Book2";
    private static final String description = "Description1";
    private static final String description2 = "Description2";
    private static final BigDecimal price = BigDecimal.valueOf(100.0);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    Book initialBook;
    Author initialAuthor;
    Genre initialGenre;

    @BeforeEach
    void setUp() {
        var books = bookRepository.findAll();
        assertThat(books).isNotNull();
        assertThat(books.size()).isGreaterThan(0);

        initialBook = books.get(0);
        initialAuthor = initialBook.getAuthor();
        initialGenre = initialBook.getGenre();
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

        Book createdBook = bookRepository.save(book);

        assertThat(createdBook).isNotNull();
        assertThat(createdBook.getTitle()).isEqualTo(title);
        assertThat(createdBook.getDescription()).isEqualTo(description);
        assertThat(createdBook.getId().length()).isGreaterThan(0);

        Book actual = bookRepository.findById(createdBook.getId()).orElse(null);

        assertThat(actual).isNotNull();
        assertThat(actual.getTitle()).isEqualTo(title);
        assertThat(actual.getDescription()).isEqualTo(description);
        assertThat(actual.getId().length()).isGreaterThan(0);

        assertThat(actual.getAuthor()).isNotNull();
        assertThat(actual.getAuthor().getId()).isEqualTo(initialAuthor.getId());

        assertThat(actual.getGenre()).isNotNull();
        assertThat(actual.getGenre().getId()).isEqualTo(initialGenre.getId());
    }

    @Test
    void shouldUpdateBook() {
        // enable statistics
        Book bookForUpdate = initialBook.toBuilder()
                .title(title2)
                .description(description2)
                .build();

        Book updatedBook = bookRepository.save(bookForUpdate);

        Book updatedActual = bookRepository.findById(updatedBook.getId()).orElse(null);

        assertThat(updatedActual).isNotNull();
        assertThat(updatedActual.getTitle()).isEqualTo(title2);
        assertThat(updatedActual.getDescription()).isEqualTo(description2);
        assertThat(updatedActual.getId().length()).isGreaterThan(0);
        assertThat(updatedActual.getId()).isEqualTo(initialBook.getId());

        assertThat(updatedActual.getAuthor()).isNotNull();
        assertThat(updatedActual.getAuthor().getId()).isEqualTo(initialAuthor.getId());

        assertThat(updatedActual.getGenre()).isNotNull();
        assertThat(updatedActual.getGenre().getId()).isEqualTo(initialGenre.getId());
    }

    @Test
    void shouldFindById() {
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
        assertThat(actual.get().getId().length()).isGreaterThan(0);

        assertThat(actual.get().getAuthor()).isNotNull();
        assertThat(actual.get().getAuthor().getId()).isEqualTo(initialAuthor.getId());

        assertThat(actual.get().getGenre()).isNotNull();
        assertThat(actual.get().getGenre().getId()).isEqualTo(initialGenre.getId());
    }

    @Test
    void shouldFindAllBooks() {
        Comment comment = Comment.builder()
                .text("Comment1")
                .book(initialBook)
                .build();

        commentRepository.save(comment);

        List<Book> books = bookRepository.findAll();

        assertThat(books).isNotEmpty();
        assertThat(books).hasSize(4);
    }

    @Test
    void shouldDeleteById() {
        mongoTemplate.dropCollection(Comment.class);

        bookRepository.deleteById(initialBook.getId());

        Optional<Book> actual = bookRepository.findById(initialBook.getId());

        assertThat(actual).isNotPresent();
    }
}
