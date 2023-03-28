package com.otus.bookstore.exception;

public class EntitySaveException extends RuntimeException {
    private final Object entity;

    public EntitySaveException(Object entity) {
        this.entity = entity;
    }

    public EntitySaveException(Object entity, Throwable cause) {
        super(cause);
        this.entity = entity;
    }

    @Override
    public String getMessage() {
        return String.format("Failed to save entity: %s", entity.toString());
    }
}
