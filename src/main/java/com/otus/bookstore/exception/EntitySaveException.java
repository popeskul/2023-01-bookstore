package com.otus.bookstore.exception;

public class EntitySaveException extends RuntimeException {
    private final Object entity;
    public final static String ERROR_SAVING_ENTITY = "Error saving entity %s";

    public EntitySaveException(Object entity) {
        this.entity = entity;
    }

    public EntitySaveException(Object entity, Throwable cause) {
        super(cause);
        this.entity = entity;
    }

    @Override
    public String getMessage() {
        return String.format(ERROR_SAVING_ENTITY, entity.toString());
    }
}
