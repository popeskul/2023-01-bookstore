package com.otus.bookstore.service;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Author;
import com.otus.bookstore.repository.AuthorRepository;
import com.otus.bookstore.service.impl.AuthorServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

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
        existedAuthor = authorService.findById(1).orElseThrow();
        existedAuthors = authorService.getAll();
    }

    @Test
    void shouldCreateAuthor() {
        Author author = unsavedValidAuthor.toBuilder().build();

        when(authorRepository.save(author)).thenReturn(author);

        // Act
        Author createdAuthor = authorService.create(author);

        // Assert
        assertThat(createdAuthor).isNotNull();
        assertThat(createdAuthor.getId()).isPositive();
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
        assertThrows(EntitySaveException.class, () -> authorService.create(author));

        // Assert
        assertThatThrownBy(() -> authorService.create(author))
                .isInstanceOf(EntitySaveException.class);
    }

    @Test
    @DisplayName("Should not create when author is null")
    void shouldNotCreateWhenAuthorNull() {
        Author author = null;
        when(authorRepository.save(author)).thenThrow(new IllegalArgumentException(AuthorServiceImpl.ERROR_AUTHOR_NULL));

        // Act
        assertThrows(IllegalArgumentException.class, () -> authorService.create(author));

        // Assert
        assertThatThrownBy(() -> authorService.create(author))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(AuthorServiceImpl.ERROR_AUTHOR_NULL);
    }

    @Test
    @DisplayName("Should not create when id is 0")
    void shouldNotCreateWhenIdIsZero() {
        Author author = unsavedValidAuthor.toBuilder().id(1L).build();
        when(authorRepository.save(author)).thenThrow(new EntitySaveException(author));

        // Act
        assertThrows(EntitySaveException.class, () -> authorService.create(author));

        // Assert
        assertThatThrownBy(() -> authorService.create(author))
                .isInstanceOf(EntitySaveException.class)
                .hasMessage(new EntitySaveException(AuthorServiceImpl.ERROR_AUTHOR_ALREADY_EXISTS).getMessage());
    }

    @Test
    void shouldGetAuthorById() {
        // Act
        Optional<Author> actual = authorService.findById(1);

        // Assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo(existedAuthor.getName());
        assertThat(actual.get().getEmail()).isEqualTo(existedAuthor.getEmail());
    }

    @Test
    void shouldGetAllAuthors() {
        // Arrange
        Author author1 = unsavedValidAuthor.toBuilder().build();
        Author createAuthor1 = authorService.create(author1);

        assertThat(createAuthor1).isNotNull();
        assertThat(createAuthor1.getId()).isPositive();

        Author author2 = unsavedValidAuthor.toBuilder().build();
        Author createAuthor2 = authorService.create(author2);

        assertThat(createAuthor2).isNotNull();
        assertThat(createAuthor2.getId()).isPositive();

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
        Author createdAuthor = authorService.create(newAuthor);

        assertThat(createdAuthor).isNotNull();
        assertThat(createdAuthor.getId()).isPositive();

        Author dirtyAuthor = newAuthor.toBuilder()
                .name(name2)
                .email(email2)
                .build();

        // Act
        authorService.update(dirtyAuthor);

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

        assertThrows(Exception.class, () -> authorService.update(wrongAuthorForUpdate));

        // Assert
        assertThatThrownBy(() -> authorService.update(wrongAuthorForUpdate))
                .isInstanceOf(Exception.class);
    }

    @Test
    @Transactional
    void shouldDeleteAuthorById() {
        // create author
        Author newAuthor = unsavedValidAuthor.toBuilder().build();

        Author createdAuthor = authorService.create(newAuthor);

        assertThat(createdAuthor).isNotNull();
        assertThat(createdAuthor.getId()).isPositive();

        Optional<Author> createAuthor = authorService.findById(createdAuthor.getId());

        assertThat(createAuthor).isPresent();

        // Act
        authorService.deleteById(createAuthor.get().getId());

        // Assert
        Optional<Author> actual = authorService.findById(createAuthor.get().getId());

        assertThat(actual).isNotPresent();
    }
}