package com.otus.bookstore.service;

import com.github.cloudyrock.spring.v5.EnableMongock;
import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Author;
import com.otus.bookstore.repository.AuthorRepository;
import com.otus.bookstore.service.impl.AuthorServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@DataMongoTest
@EnableMongock
@ComponentScan({
        "com.otus.bookstore.repository",
        "com.otus.bookstore.validator"
})
@ActiveProfiles("test")
@Import({AuthorServiceImpl.class})
class AuthorServiceImplTest {
    private static final String name = "Some Doe";
    private static final String name2 = "Some Doe2";
    private static final String email = "email1@mail.com";
    private static final String email2 = "email2@mail.com";

    Author unsavedValidAuthor = Author.builder()
            .name(name)
            .email(email)
            .build();

    Author existedAuthor;
    List<Author> existedAuthors;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Mock
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        existedAuthor = mongoTemplate.findAll(Author.class).get(0);
        assertThat(existedAuthor).isNotNull();
        existedAuthors = authorService.getAll();
    }

    @Test
    void shouldCreateAuthor() {
        Author author = unsavedValidAuthor.toBuilder().build();

        when(authorRepository.save(author)).thenReturn(author);

        // Act
        Author createdAuthor = authorService.save(author);

        // Assert
        assertThat(createdAuthor).isNotNull();
        assertThat(createdAuthor.getId().length()).isGreaterThan(0);
        assertThat(createdAuthor.getName()).isEqualTo(name);

        Optional<Author> actual = authorService.findById(createdAuthor.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo(name);
        assertThat(actual.get().getEmail()).isEqualTo(email);
    }

    @Test
    void shouldNotCreateAuthorWithInvalidData() {
        Author author = unsavedValidAuthor.toBuilder().email(null).name(null).build();

        when(authorRepository.save(author)).thenThrow(EntitySaveException.class);

        // Act
        assertThrows(ConstraintViolationException.class, () -> authorService.save(author));
    }

    @Test
    @DisplayName("Should not create when author is null")
    void shouldNotCreateWhenAuthorNull() {
        Author author = null;
        when(authorRepository.save(author)).thenThrow(new IllegalArgumentException());

        // Act
        assertThrows(IllegalArgumentException.class, () -> authorService.save(author));

        // Assert
        assertThatThrownBy(() -> authorService.save(author))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldGetAuthorById() {
        // Act
        Optional<Author> actual = authorService.findById(existedAuthor.getId());

        // Assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo(existedAuthor.getName());
        assertThat(actual.get().getEmail()).isEqualTo(existedAuthor.getEmail());
    }

    @Test
    void shouldGetAllAuthors() {
        // Arrange
        Author author1 = unsavedValidAuthor.toBuilder().build();
        Author createAuthor1 = authorService.save(author1);

        assertThat(createAuthor1).isNotNull();
        assertThat(createAuthor1.getId().length()).isGreaterThan(0);

        Author author2 = unsavedValidAuthor.toBuilder().name(name2).email(email2).build();
        Author createAuthor2 = authorService.save(author2);

        assertThat(createAuthor2).isNotNull();
        assertThat(createAuthor2.getId().length()).isGreaterThan(0);

        // Act
        List<Author> authors = authorService.getAll();

        // Assert
        assertThat(authors).isNotNull();

        // find by name and email
        Optional<Author> actual1 = authors.stream()
                .filter(a -> a.getName().equals(name) && a.getEmail().equals(email))
                .findFirst();

        assertThat(actual1).isPresent();

        // find by name and email
        Optional<Author> actual2 = authors.stream()
                .filter(a -> a.getName().equals(name2) && a.getEmail().equals(email2))
                .findFirst();

        assertThat(actual2).isPresent();
    }

    @Test
    void shouldUpdateAuthor() {
        Author newAuthor = unsavedValidAuthor.toBuilder().build();
        Author createdAuthor = authorService.save(newAuthor);

        assertThat(createdAuthor).isNotNull();
        assertThat(createdAuthor.getId().length()).isGreaterThan(0);

        Author dirtyAuthor = newAuthor.toBuilder()
                .id(createdAuthor.getId())
                .name(name2)
                .email(email2)
                .build();

        // Act
        Author updatedAuthor = authorService.save(dirtyAuthor);

        // Assert
        Optional<Author> actual = authorService.findById(createdAuthor.getId());

        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo(name2);
        assertThat(actual.get().getEmail()).isEqualTo(email2);
    }


    @Test
    void shouldNotUpdateAuthorWithInvalidData() {
        // Arrange
        Author earlyCreatedAuthor = existedAuthor.toBuilder().build();
        Author wrongAuthorForUpdate = earlyCreatedAuthor.toBuilder().name(null).email(null).build();

        assertThrows(Exception.class, () -> authorService.save(wrongAuthorForUpdate));

        // Assert
        assertThatThrownBy(() -> authorService.save(wrongAuthorForUpdate))
                .isInstanceOf(Exception.class);
    }

    @Test
    void shouldDeleteAuthorById() {
        // create author
        Author newAuthor = unsavedValidAuthor.toBuilder().build();

        Author createdAuthor = authorService.save(newAuthor);

        assertThat(createdAuthor).isNotNull();
        assertThat(createdAuthor.getId().length()).isGreaterThan(0);

        Optional<Author> createAuthor = authorService.findById(createdAuthor.getId());

        assertThat(createAuthor).isPresent();

        // Act
        authorService.deleteById(createAuthor.get().getId());

        // Assert
        Optional<Author> actual = authorService.findById(createAuthor.get().getId());

        assertThat(actual).isNotPresent();
    }
}