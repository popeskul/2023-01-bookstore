package com.otus.bookstore.events;

import com.otus.bookstore.model.Book;
import com.otus.bookstore.repository.CommentRepository;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

@Component
public class BookCascadeDeleteMongoEventListener extends AbstractMongoEventListener<Book> {
    private final CommentRepository commentRepository;

    public BookCascadeDeleteMongoEventListener(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        String bookId = event.getSource().get("_id").toString();
        commentRepository.findById(bookId).ifPresent(commentRepository::delete);
    }
}
