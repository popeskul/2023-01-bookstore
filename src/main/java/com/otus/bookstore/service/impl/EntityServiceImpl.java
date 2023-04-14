package com.otus.bookstore.service.impl;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.validator.ObjectValidator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;
import java.util.logging.Logger;

public abstract class EntityServiceImpl<T> {
    Logger logger = Logger.getLogger(EntityServiceImpl.class.getName());
    private final CrudRepository<T, String> repository;
    private final ObjectValidator validator;

    public EntityServiceImpl(CrudRepository<T, String> repository, ObjectValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public T save(T entity) {
        logger.info("Saving entity: " + entity);
        Set<? extends ConstraintViolation<?>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        try {
            var savedEntity = repository.save(entity);
            logger.info("Entity saved: " + savedEntity);
            return savedEntity;
        } catch (Exception e) {
            logger.severe("Error saving entity: " + e.getMessage());
            throw new EntitySaveException(entity, e);
        }
    }
}
