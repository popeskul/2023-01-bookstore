package com.otus.bookstore.repository.impl;

import com.otus.bookstore.model.Author;
import com.otus.bookstore.repository.AuthorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryJpa implements AuthorRepository {
    @PersistenceContext
    private final EntityManager em;

    public AuthorRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Author> findById(int id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public List<Author> findAll() {
        TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public Optional<Author> save(Author author) {
        try {
            if (author.getId() == 0) {
                em.persist(author);
                return Optional.of(author);
            } else {
                return Optional.of(em.merge(author));
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(int id) {
        Author author = em.find(Author.class, id);
        if (author != null) {
            em.remove(author);
        }
    }
}
