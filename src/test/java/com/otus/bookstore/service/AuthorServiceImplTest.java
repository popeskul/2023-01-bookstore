package com.otus.bookstore.service;

import com.otus.bookstore.exception.AuthorBadRequestException;
import com.otus.bookstore.model.Author;
import com.otus.bookstore.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import({AuthorServiceImpl.class})
class AuthorServiceImplTest {
    private static final String name = "Some Doe";
    private static final String name2 = "Some Doe2";
    private static final String email = "email1@mail.com";
    private static final String email2 = "email2@mail.com";

    Author unsavedAuthor = Author.builder()
            .name(name)
            .email(email)
            .build();

    @Autowired
    private AuthorService authorService;

    @Test
    void shouldCreateAuthor() {
        // Arrange
        Author author = unsavedAuthor.toBuilder().build();

        // Act
        Optional<Integer> id = authorService.create(author);

        // Assert
        assertThat(id).isPresent();
        assertThat(id.get()).isPositive();

        Optional<Author> actual = authorService.getById(id.get());
        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo(name);
        assertThat(actual.get().getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Should not create author with invalid data")
    void shouldNotCreateAuthorWithInvalidData() {
        Author author = Author.builder().build();

        assertThatThrownBy(() -> authorService.create(author))
                .isInstanceOf(AuthorBadRequestException.class)
                .hasMessage(AuthorBadRequestException.ERROR_BAD_REQUEST);
    }

    @Test
    void shouldGetAuthorById() {
        // create author
        Optional<Integer> id = authorService.create(unsavedAuthor);

        assertThat(id).isPresent();
        assertThat(id.get()).isPositive();

        // Act
        Optional<Author> actual = authorService.getById(id.get());

        // Assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo(name);
        assertThat(actual.get().getEmail()).isEqualTo(email);
    }

    @Test
    void shouldGetAllAuthors() {
        // Arrange
        Author author1 = unsavedAuthor.toBuilder().build();
        Optional<Integer> id1 = authorService.create(author1);

        assertThat(id1).isPresent();
        assertThat(id1.get()).isPositive();

        Author author2 = unsavedAuthor.toBuilder().build();
        Optional<Integer> id2 = authorService.create(author2);

        assertThat(id2).isPresent();
        assertThat(id2.get()).isPositive();

        // Act
        List<Author> authors = authorService.getAll();

        // Assert
        assertThat(authors).isNotNull();
        assertThat(authors).contains(author1);
        assertThat(authors).contains(author2);
    }

    @Test
    void shouldUpdateAuthor() {
        Author newAuthor = unsavedAuthor.toBuilder().build();
        Optional<Integer> id = authorService.create(newAuthor);

        assertThat(id).isPresent();
        assertThat(id.get()).isPositive();

        Author dirtyAuthor = newAuthor.toBuilder()
                .name(name2)
                .email(email2)
                .build();

        // Act
        authorService.update(dirtyAuthor);

        // Assert
        Optional<Author> actual = authorService.getById(id.get());

        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo(name2);
        assertThat(actual.get().getEmail()).isEqualTo(email2);
    }

    @Test
    void shouldNotUpdateAuthorWithInvalidData() {
        Author newAuthor = unsavedAuthor.toBuilder().build();
        Optional<Integer> id = authorService.create(newAuthor);

        assertThat(id).isPresent();
        assertThat(id.get()).isPositive();

        Optional<Author> createdAuthor = authorService.getById(id.get());

        assertThat(createdAuthor).isPresent();

        // Arrange
        Author dirtyAuthor = createdAuthor.get().toBuilder()
                .id(0)
                .name(null)
                .email(null)
                .build();

        // Act
        assertThatThrownBy(() -> authorService.update(dirtyAuthor))
                .isInstanceOf(AuthorBadRequestException.class)
                .hasMessage(AuthorBadRequestException.ERROR_BAD_REQUEST);
    }

    @Test
    void shouldDeleteAuthorById() {
        // create author
        Author newAuthor = unsavedAuthor.toBuilder().build();

        Optional<Integer> id = authorService.create(newAuthor);

        assertThat(id).isPresent();
        assertThat(id.get()).isPositive();

        Optional<Author> createAuthor = authorService.getById(id.get());

        assertThat(createAuthor).isPresent();

        // Act
        authorService.deleteById(createAuthor.get().getId());

        // Assert
        Optional<Author> actual = authorService.getById(createAuthor.get().getId());

        assertThat(actual).isNotPresent();
    }
}