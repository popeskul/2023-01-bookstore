package com.otus.bookstore.repository;

import com.otus.bookstore.model.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
@ComponentScan({"com.otus.bookstore.repository", "com.otus.bookstore.model"})
class AuthorRepositoryTest {
    private static final String ID = "1";
    private static final String name = "Some Doe";
    private static final String name2 = "Some Doe2";
    private static final String email = "some@mail.com";
    private static final String email2 = "some2@mail.com";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final Author validAuthorWithId = Author.builder()
            .id(ID)
            .name(name)
            .email(email)
            .build();

    @Test
    void shouldCreateAuthor() {
        Author author = validAuthorWithId.toBuilder().build();

        authorRepository.save(author);

        mongoTemplate.findAll(Author.class).forEach(System.out::println);
        Author actual = mongoTemplate.findById(ID, Author.class);

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getId().length()).isGreaterThan(0);
        assertThat(actual.getEmail()).isEqualTo(email);
    }

    @Test
    void shouldUpdateAuthor() {
        Author author = validAuthorWithId.toBuilder().build();

        authorRepository.save(author);

        Author actual = mongoTemplate.findById(ID, Author.class);

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getId().length()).isGreaterThan(0);
        assertThat(actual.getEmail()).isEqualTo(email);

        Author dirty = actual.toBuilder().name(name2).email(email2).build();

        authorRepository.save(dirty);

        Author updated = mongoTemplate.findById(ID, Author.class);

        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo(name2);
        assertThat(updated.getId().length()).isGreaterThan(0);
        assertThat(updated.getEmail()).isEqualTo(email2);
    }

    @Test
    public void shouldGetAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).isNotNull();
    }

    @Test
    public void shouldOnFindAllAuthorsItFoundNothing() {
        mongoTemplate.getCollection("author").drop();
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).isNotNull();
    }

    @Test
    public void shouldFindById() {
        Author author = validAuthorWithId.toBuilder().build();

        Author savedAuthor = authorRepository.save(author);

        Optional<Author> actual = authorRepository.findById(savedAuthor.getId());

        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo(name);
        assertThat(actual.get().getId().length()).isGreaterThan(0);
        assertThat(actual.get().getEmail()).isEqualTo(email);
    }

    @Test
    void shouldNotFindAuthorById() {
        Optional<Author> author = authorRepository.findById("0");

        assertThat(author).isEmpty();
    }

    @Test
    void shouldDeleteAuthorById() {
        // delete book first to avoid constraint violation
        mongoTemplate.getCollection("comment").drop();
        mongoTemplate.getCollection("book").drop();

        // Arrange
        Author author = Author.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .build();
        mongoTemplate.save(author);

        // Act
        authorRepository.deleteById(author.getId());

        // Assert
        assertThat(mongoTemplate.findById(author.getId(), Author.class)).isNull();
    }

    @Test
    void shouldNotDeleteAuthorById() {
        // delete book first to avoid constraint violation
        mongoTemplate.getCollection("comment").drop();
        mongoTemplate.getCollection("book").drop();
        mongoTemplate.getCollection("author").drop();

        // Act
        authorRepository.deleteById("0");

        // Assert
        assertThat(mongoTemplate.findById("0", Author.class)).isNull();
    }
}