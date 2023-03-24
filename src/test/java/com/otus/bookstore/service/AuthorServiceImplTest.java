package com.otus.bookstore.service;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Author;
import com.otus.bookstore.repository.AuthorRepository;
import com.otus.bookstore.service.impl.AuthorServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import({AuthorServiceImpl.class})
class AuthorServiceImplTest {
    private static final String name = "Some Doe";
    private static final String name2 = "Some Doe2";
    private static final String email = "email1@mail.com";
    private static final String email2 = "email2@mail.com";

    Author unsavedValidAuthor = Author.builder()
            .id(0L)
            .name(name)
            .email(email)
            .build();

    Author existedAuthor;
    List<Author> existedAuthors;

    @Autowired
    private AuthorService authorService;

    @Mock
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        existedAuthor = authorService.getById(1).orElseThrow();
        existedAuthors = authorService.getAll();
    }

    @Test
    void shouldCreateAuthor() {
        Author author = unsavedValidAuthor.toBuilder().build();

        when(authorRepository.save(author)).thenReturn(Optional.of(author));

        // Act
        Optional<Long> id = authorService.create(author);

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
                .isInstanceOf(EntitySaveException.class)
                .hasMessage(new EntitySaveException(author).getMessage());
    }

    @Test
    void shouldGetAuthorById() {
        // Act
        Optional<Author> actual = authorService.getById(1);

        // Assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo(existedAuthor.getName());
        assertThat(actual.get().getEmail()).isEqualTo(existedAuthor.getEmail());
    }

    @Test
    void shouldGetAllAuthors() {
        // Arrange
        Author author1 = unsavedValidAuthor.toBuilder().build();
        Optional<Long> id1 = authorService.create(author1);

        assertThat(id1).isPresent();
        assertThat(id1.get()).isPositive();

        Author author2 = unsavedValidAuthor.toBuilder().build();
        Optional<Long> id2 = authorService.create(author2);

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
        Author newAuthor = unsavedValidAuthor.toBuilder().build();
        Optional<Long> id = authorService.create(newAuthor);

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
        // Arrange
        Author earlyCreatedAuthor = existedAuthor.toBuilder().build();
        Author wrongAuthorForUpdate = earlyCreatedAuthor.toBuilder().name(null).email(null).build();

        // Act
        assertThatThrownBy(() -> authorService.update(wrongAuthorForUpdate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @Transactional
    void shouldDeleteAuthorById() {
        // create author
        Author newAuthor = unsavedValidAuthor.toBuilder().build();

        Optional<Long> id = authorService.create(newAuthor);

        assertThat(id).isPresent();
        assertThat(id.get()).isPositive();

        Optional<Author> createAuthor = authorService.getById(id.get());

        assertThat(createAuthor).isPresent();

        // Act
        authorService.deleteById(createAuthor.get().getId());

        // Assert
        assertThrows(EntityNotFoundException.class, () -> authorService.getById(createAuthor.get().getId()));
    }
}