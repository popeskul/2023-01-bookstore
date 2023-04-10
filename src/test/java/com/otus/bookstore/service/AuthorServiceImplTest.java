package com.otus.bookstore.service;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Author;
import com.otus.bookstore.repository.AuthorRepository;
import com.otus.bookstore.service.impl.AuthorServiceImpl;
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
        Author createdAuthor = authorService.save(author);

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
        assertThrows(EntitySaveException.class, () -> authorService.save(author));
    }

    @Test
    @DisplayName("Should not create when author is null")
    void shouldNotCreateWhenAuthorNull() {
        Author author = null;
        when(authorRepository.save(author)).thenThrow(new EntitySaveException(author));

        // Act
        assertThrows(EntitySaveException.class, () -> authorService.save(author));

        // Assert
        assertThatThrownBy(() -> authorService.save(author))
                .isInstanceOf(EntitySaveException.class);
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
        Author createAuthor1 = authorService.save(author1);

        assertThat(createAuthor1).isNotNull();
        assertThat(createAuthor1.getId()).isPositive();

        Author author2 = unsavedValidAuthor.toBuilder().name(name2).email(email2).build();
        Author createAuthor2 = authorService.save(author2);

        assertThat(createAuthor2).isNotNull();
        assertThat(createAuthor2.getId()).isPositive();

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
        assertThat(createdAuthor.getId()).isPositive();

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