package com.otus.bookstore.dao;

import com.otus.bookstore.dao.impl.AuthorDaoJdbc;
import com.otus.bookstore.dao.impl.BookDaoJdbc;
import com.otus.bookstore.dao.impl.GenreDaoJdbc;
import com.otus.bookstore.model.Author;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.model.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testing BookDaoJdbc")
@JdbcTest
@Import({AuthorDaoJdbc.class, GenreDaoJdbc.class, BookDaoJdbc.class})
@ComponentScan({
        "com.otus.bookstore.service.book",
        "com.otus.bookstore.service.author",
        "com.otus.bookstore.service.genre"
})
class BookDaoJdbcTest {
    private final int bookId = 1;
    private final int authorId = 1;
    private final int genreId = 1;
    private final String bookTitle = "Book title";
    private final String bookTitle2 = "Book title 2";
    private final String bookDescription = "Book description";
    private final String bookDescription2 = "Book description 2";
    private final BigDecimal bookPrice = BigDecimal.valueOf(100.0);

    @Autowired
    private AuthorDaoJdbc authorDao;

    @Autowired
    private BookDaoJdbc bookDao;

    @Autowired
    private GenreDaoJdbc genreDao;

    @Test
    void shouldInsertBook() {
        final Optional<Author> savedAuthor = authorDao.getById(authorId);

        assertThat(savedAuthor).isNotNull();
        assertThat(savedAuthor.isPresent()).isTrue();

        final Optional<Integer> saveAuthorId = authorDao.insert(savedAuthor.get());

        assertThat(saveAuthorId).isNotNull();
        assertThat(saveAuthorId.isPresent()).isTrue();
        assertThat(saveAuthorId.get()).isGreaterThan(0);

        Optional<Genre> savedGenre = genreDao.getById(genreId);

        assertThat(savedGenre).isNotNull();
        assertThat(savedGenre.isPresent()).isTrue();

        final Optional<Integer> savedBookId = bookDao.insert(Book.builder()
                .title(bookTitle)
                .description(bookDescription)
                .price(bookPrice)
                .author(savedAuthor.get())
                .genre(savedGenre.get())
                .build());

        assertThat(savedBookId).isNotNull();
        assertThat(savedBookId.isPresent()).isTrue();
        assertThat(savedBookId.get()).isGreaterThan(0);

        final Optional<Book> savedBook = bookDao.getById(savedBookId.get());

        assertThat(savedBook).isNotNull();
        assertThat(savedBook.isPresent()).isTrue();
        assertThat(savedBook.get().getId()).isGreaterThan(0);
        assertThat(savedBook.get().getTitle()).isEqualTo(bookTitle);
        assertThat(savedBook.get().getDescription()).isEqualTo(bookDescription);

        assertEquals(savedBook.get().getPrice().longValue(), bookPrice.longValue());

        assertThat(savedBook.get().getAuthor().getId()).isGreaterThan(0);
        assertThat(savedBook.get().getAuthor().getName()).isEqualTo(savedAuthor.get().getName());
        assertThat(savedBook.get().getAuthor().getEmail()).isEqualTo(savedAuthor.get().getEmail());

        assertThat(savedBook.get().getGenre()).isNotNull();
        assertThat(savedBook.get().getGenre().getId()).isGreaterThan(0);
        assertThat(savedBook.get().getGenre().getName()).isEqualTo(savedGenre.get().getName());
    }

    @Test
    void shouldUpdateBook() {
        // get book
        final Optional<Book> initialBook = bookDao.getById(bookId);

        assertThat(initialBook).isNotNull();
        assertThat(initialBook.isPresent()).isTrue();

        System.out.println("initialBook = " + initialBook.get());

        // update book
        final Book dirtyUnsavedBook = initialBook.get().toBuilder()
                .title(bookTitle2)
                .description(bookDescription2)
                .build();

        System.out.println("dirtyUnsavedBook = " + dirtyUnsavedBook);

        bookDao.update(dirtyUnsavedBook);

        // get updated book
        final Optional<Book> updatedBook = bookDao.getById(bookId);

        System.out.println("updatedBook = " + updatedBook.get());

        assertThat(updatedBook).isNotNull();
        assertThat(updatedBook.isPresent()).isTrue();

        // check updated book
        assertThat(updatedBook.get().getId()).isEqualTo(bookId);
        assertThat(updatedBook.get().getTitle()).isEqualTo(bookTitle2);
        assertThat(updatedBook.get().getDescription()).isEqualTo(bookDescription2);

        // check initial book author
        assertThat(updatedBook.get().getAuthor().getId()).isEqualTo(initialBook.get().getAuthor().getId());
        assertThat(updatedBook.get().getAuthor().getName()).isEqualTo(initialBook.get().getAuthor().getName());

        // check initial book genre
        assertThat(updatedBook.get().getGenre().getId()).isEqualTo(initialBook.get().getGenre().getId());
        assertThat(updatedBook.get().getGenre().getName()).isEqualTo(initialBook.get().getGenre().getName());

        // check initial book price
        assertEquals(updatedBook.get().getPrice(), initialBook.get().getPrice());
    }

    @Test
    void shouldDeleteBookById() {
        final List<Book> booksBefore = bookDao.getAll();

        final Optional<Author> author = authorDao.getById(1);

        assertThat(author).isNotNull();
        assertThat(author.isPresent()).isTrue();

        final Optional<Genre> genre = genreDao.getById(1);

        assertThat(genre).isNotNull();
        assertThat(genre.isPresent()).isTrue();

        final Optional<Integer> savedBookId = bookDao.insert(Book.builder()
                .title(bookTitle)
                .description(bookDescription)
                .price(bookPrice)
                .author(author.get())
                .genre(genre.get())
                .build());

        assertThat(savedBookId).isNotNull();
        assertThat(savedBookId.isPresent()).isTrue();
        assertThat(savedBookId.get()).isGreaterThan(0);

        final List<Book> booksAfter = bookDao.getAll();

        assertThat(booksAfter.size()).isEqualTo(booksBefore.size() + 1);

        bookDao.deleteById(savedBookId.get());

        final List<Book> booksAfterDelete = bookDao.getAll();

        assertThat(booksAfterDelete.size()).isEqualTo(booksBefore.size());
    }

    @Test
    void shouldGetBookById() {
        // save book
        final Optional<Author> author = authorDao.getById(1);

        assertThat(author).isNotNull();
        assertThat(author.isPresent()).isTrue();

        final Optional<Genre> genre = genreDao.getById(1);

        assertThat(genre).isNotNull();
        assertThat(genre.isPresent()).isTrue();

        final Optional<Integer> savedBookId = bookDao.insert(Book.builder()
                .title(bookTitle)
                .description(bookDescription)
                .price(bookPrice)
                .author(author.get())
                .genre(genre.get())
                .build());

        assertThat(savedBookId).isNotNull();
        assertThat(savedBookId.isPresent()).isTrue();
        assertThat(savedBookId.get()).isGreaterThan(0);

        // get saved book
        final Optional<Book> savedBook = bookDao.getById(savedBookId.get());

        assertThat(savedBook).isNotNull();
        assertThat(savedBook.isPresent()).isTrue();

        // check saved book
        assertThat(savedBook.get().getId()).isGreaterThan(0);
        assertThat(savedBook.get().getTitle()).isEqualTo(bookTitle);
        assertThat(savedBook.get().getDescription()).isEqualTo(bookDescription);
        assertThat(savedBook.get().getPrice().longValue()).isEqualTo(bookPrice.longValue());

        assertThat(savedBook.get().getAuthor().getId()).isGreaterThan(0);
        assertThat(savedBook.get().getAuthor().getName()).isEqualTo(author.get().getName());
        assertThat(savedBook.get().getAuthor().getEmail()).isEqualTo(author.get().getEmail());

        assertThat(savedBook.get().getGenre().getId()).isGreaterThan(0);
        assertThat(savedBook.get().getGenre().getName()).isEqualTo(genre.get().getName());
    }

    @Test
    void shouldGetAllBooks() {
        final List<Book> booksBefore = bookDao.getAll();

        final Optional<Author> author = authorDao.getById(1);

        assertThat(author).isNotNull();
        assertThat(author.isPresent()).isTrue();

        final Optional<Genre> genre = genreDao.getById(1);

        assertThat(genre).isNotNull();
        assertThat(genre.isPresent()).isTrue();

        final Optional<Integer> savedBookId = bookDao.insert(Book.builder()
                .title(bookTitle)
                .description(bookDescription)
                .price(bookPrice)
                .author(author.get())
                .genre(genre.get())
                .build());

        assertThat(savedBookId).isNotNull();
        assertThat(savedBookId.isPresent()).isTrue();

        final List<Book> booksAfter = bookDao.getAll();

        assertThat(booksAfter.size()).isEqualTo(booksBefore.size() + 1);
        assertThat(booksAfter.get(booksAfter.size() - 1).getId()).isEqualTo(savedBookId.get());
    }

    @Test
    void shouldDeleteAllBooks() {
        final List<Book> booksBefore = bookDao.getAll();

        final Optional<Author> author = authorDao.getById(1);

        assertThat(author).isNotNull();
        assertThat(author.isPresent()).isTrue();

        final Optional<Genre> genre = genreDao.getById(1);

        assertThat(genre).isNotNull();
        assertThat(genre.isPresent()).isTrue();

        final Optional<Integer> savedBookId = bookDao.insert(Book.builder()
                .title(bookTitle)
                .description(bookDescription)
                .price(bookPrice)
                .author(author.get())
                .genre(genre.get())
                .build());

        assertThat(savedBookId).isNotNull();
        assertThat(savedBookId.isPresent()).isTrue();
        assertThat(savedBookId.get()).isGreaterThan(0);

        final List<Book> booksAfter = bookDao.getAll();

        assertThat(booksAfter.size()).isEqualTo(booksBefore.size() + 1);

        bookDao.deleteAll();

        final List<Book> booksAfterDelete = bookDao.getAll();

        assertThat(booksAfterDelete.size()).isEqualTo(0);
    }
}
