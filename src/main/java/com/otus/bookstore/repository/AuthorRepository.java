package com.otus.bookstore.repository;

import com.otus.bookstore.model.Author;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorRepository extends MongoRepository<Author, String> {
}