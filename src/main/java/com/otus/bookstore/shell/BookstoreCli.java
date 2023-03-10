package com.otus.bookstore.shell;

import com.otus.bookstore.dao.AuthorDao;
import com.otus.bookstore.dao.BookDao;
import com.otus.bookstore.dao.GenreDao;
import com.otus.bookstore.model.Author;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class BookstoreCli {
    private static String ERROR_AUTHOR_NOT_FOUND = "Author not found";
    private static String ERROR_GENRE_NOT_FOUND = "Genre not found";

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    @ShellMethod(value = "Get all books", key = {"book list"})
    public List<Book> getAllBooks() {
        return bookDao.getAll();
    }

    @ShellMethod(value = "Get book by id", key = {"book find"})
    public Optional<Book> getBookById(
            @ShellOption(defaultValue = "id") String id
    ) {
        return bookDao.getById(Integer.parseInt(id));
    }

    @ShellMethod(value = "Create book", key = {"book create"})
    public Optional<Integer> createBook(
            @ShellOption(defaultValue = "title") String title,
            @ShellOption(defaultValue = "description") String description,
            @ShellOption(defaultValue = "price") String price,
            @ShellOption(defaultValue = "authorId") String authorId,
            @ShellOption(defaultValue = "genreId") String genreId
    ) {
        Author author = authorDao.getById(Integer.parseInt(authorId))
                .orElseThrow(() -> new RuntimeException(ERROR_AUTHOR_NOT_FOUND));

        Genre genre = genreDao.getById(Integer.parseInt(genreId))
                .orElseThrow(() -> new RuntimeException(ERROR_GENRE_NOT_FOUND));

        Book dirtyBook = Book.builder()
                .title(title)
                .description(description)
                .price(BigDecimal.valueOf(Double.parseDouble(price)))
                .author(author)
                .genre(genre)
                .build();

        return bookDao.insert(dirtyBook);
    }

    @ShellMethod(value = "Update book", key = {"book update"})
    public void updateBook(
            @ShellOption(defaultValue = "id") String id,
            @ShellOption(defaultValue = "title") String title,
            @ShellOption(defaultValue = "description") String description,
            @ShellOption(defaultValue = "price") String price,
            @ShellOption(defaultValue = "authorId") String authorId,
            @ShellOption(defaultValue = "genreId") String genreId
    ) {
        bookDao.update(Book.builder()
                .id(Integer.parseInt(id))
                .title(title)
                .description(description)
                .price(BigDecimal.valueOf(Double.parseDouble(price)))
                .author(Author.builder().id(Integer.parseInt(authorId)).build())
                .genre(Genre.builder().id(Integer.parseInt(genreId)).build())
                .build());
    }

    @ShellMethod(value = "Delete book by id", key = {"book delete"})
    public void deleteBookById(
            @ShellOption(defaultValue = "id") String id
    ) {
        bookDao.deleteById(Integer.parseInt(id));
    }
}
