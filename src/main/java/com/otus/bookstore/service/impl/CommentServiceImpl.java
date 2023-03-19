package com.otus.bookstore.service.impl;

import com.otus.bookstore.exception.CommentErrorSavedException;
import com.otus.bookstore.exception.CommentNotFoundException;
import com.otus.bookstore.model.BookComment;
import com.otus.bookstore.model.BookCommentId;
import com.otus.bookstore.model.Comment;
import com.otus.bookstore.repository.BookCommentRepository;
import com.otus.bookstore.repository.CommentRepository;
import com.otus.bookstore.service.CommentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BookCommentRepository bookCommentRepository;


    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, BookCommentRepository bookCommentRepository) {
        this.commentRepository = commentRepository;
        this.bookCommentRepository = bookCommentRepository;
    }

    @Override
    public Comment save(Comment comment) throws CommentErrorSavedException {
        try {
            comment = commentRepository.save(comment);

            BookCommentId bookCommentId = BookCommentId.builder()
                    .bookId(comment.getBook().getId())
                    .commentId(comment.getId())
                    .build();
            BookComment bookComment = BookComment.builder()
                    .id(bookCommentId)
                    .book(comment.getBook())
                    .comment(comment)
                    .build();

            bookCommentRepository.save(bookComment);
            return comment;
        } catch (Exception e) {
            throw new CommentErrorSavedException();
        }
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public Optional<Comment> findById(Long id) throws CommentNotFoundException {
        try {
            Optional<Comment> commentOptional = commentRepository.findById(id);

            if (commentOptional.isEmpty()) {
                throw new CommentNotFoundException(String.format(CommentNotFoundException.ERROR_NOT_FOUND, id));
            }

            return commentOptional;
        } catch (RuntimeException e) {
            System.out.println("Comment not found 2");
            throw new CommentNotFoundException(String.format(CommentNotFoundException.ERROR_NOT_FOUND, id));
        }
    }

    @Override
    public void deleteById(Long id) throws CommentNotFoundException {
        try {
            Optional<Comment> findComment = commentRepository.findById(id);

            if (findComment.isEmpty()) {
                throw new CommentNotFoundException(String.format(CommentNotFoundException.ERROR_NOT_FOUND, id));
            }

            commentRepository.deleteById(id);

            bookCommentRepository.deleteByBookCommentId(BookCommentId.builder()
                    .bookId(findComment.get().getBook().getId())
                    .commentId(findComment.get().getId())
                    .build());
        } catch (RuntimeException e) {
            throw new CommentNotFoundException(String.format(CommentNotFoundException.ERROR_NOT_FOUND, id));
        }
    }
}
