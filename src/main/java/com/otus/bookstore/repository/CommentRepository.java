package com.otus.bookstore.repository;

import com.otus.bookstore.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {
}
