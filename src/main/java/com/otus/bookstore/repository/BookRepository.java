package com.otus.bookstore.repository;

import com.otus.bookstore.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String> {
}
