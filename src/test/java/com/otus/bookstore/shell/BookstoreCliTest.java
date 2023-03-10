package com.otus.bookstore.shell;

import com.otus.bookstore.dao.BookDao;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.utils.ShellBookPatterns;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.shell.InputProvider;
import org.springframework.shell.ResultHandlerService;
import org.springframework.shell.Shell;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookstoreCliTest {
    private final static String COMMAND_GET_ALL_BOOKS = "book list";
    private final static String COMMAND_GET_BOOK_BY_ID = "book find";
    private final static String COMMAND_CREATE_BOOK = "book create";
    private final static String COMMAND_UPDATE_BOOK = "book update";
    private final static String COMMAND_DELETE_BOOK = "book delete";

    private ArgumentCaptor<Object> argumentCaptor;

    @Mock
    private InputProvider inputProvider;

    @SpyBean
    private ResultHandlerService resultHandlerService;

    @Autowired
    private Shell shell;

    @Autowired
    private BookDao bookDao;

    @BeforeEach
    void setUp() {
        argumentCaptor = ArgumentCaptor.forClass(Object.class);

    }

    @Test
    void shouldGetAllBooks() throws Exception {
        List<Book> books = bookDao.getAll();

        when(inputProvider.readInput())
                .thenReturn(() -> COMMAND_GET_ALL_BOOKS)
                .thenReturn(null);

        shell.run(inputProvider);

        verify(resultHandlerService, times(1)).handle(argumentCaptor.capture());
        verify(resultHandlerService).handle(books);

        List<Object> results = argumentCaptor.getAllValues();

        assertThat(results).containsExactlyInAnyOrder(books);
    }

    @Test
    void shouldGetBookById() throws Exception {
        Optional<Book> book = bookDao.getById(1);

        when(inputProvider.readInput())
                .thenReturn(() -> COMMAND_GET_BOOK_BY_ID + " 1")
                .thenReturn(null);

        shell.run(inputProvider);

        verify(resultHandlerService, times(1)).handle(argumentCaptor.capture());
        verify(resultHandlerService).handle(book);

        List<Object> results = argumentCaptor.getAllValues();

        assertThat(results).containsExactlyInAnyOrder(book);
    }

    @Test
    void shouldCreateBook() throws Exception {
        int maxId = bookDao.getAll().stream()
                .mapToInt(Book::getId)
                .max()
                .orElse(0);

        assertThat(maxId).isGreaterThan(0);

        when(inputProvider.readInput())
                .thenReturn(() -> String.format(
                                ShellBookPatterns.SHELL_CREATE_BOOK_PATTERN.formatted(
                                        COMMAND_CREATE_BOOK, "title", "description", "100.0", "1", "1")
                        )
                )
                .thenReturn(null);

        shell.run(inputProvider);

        verify(resultHandlerService, times(1)).handle(argumentCaptor.capture());
        verify(resultHandlerService).handle(any(Optional.class));

        List<Object> results = argumentCaptor.getAllValues();

        assertThat(results).containsExactlyInAnyOrder(Optional.of(maxId + 1));
    }

    @Test
    void shouldUpdateBook() throws Exception {
        when(inputProvider.readInput())
                .thenReturn(() -> String.format(
                                ShellBookPatterns.SHELL_UPDATE_BOOK_PATTERN.formatted(
                                        COMMAND_UPDATE_BOOK, "1", "title", "description", "100.0", "1", "1")
                        )
                )
                .thenReturn(null);

        shell.run(inputProvider);

        verify(resultHandlerService, times(1)).handle(argumentCaptor.capture());
    }

    @Test
    void shouldDeleteBook() throws Exception {
        when(inputProvider.readInput())
                .thenReturn(() -> COMMAND_DELETE_BOOK + " 1")
                .thenReturn(null);

        shell.run(inputProvider);

        verify(resultHandlerService, times(1)).handle(argumentCaptor.capture());
    }
}