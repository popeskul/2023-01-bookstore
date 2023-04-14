package com.otus.bookstore.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class ObjectValidator {
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    public <T> Set<? extends ConstraintViolation<?>> validate(T object) {
        var violations = validator.validate(object);
        if (!violations.isEmpty()) {
            return new HashSet<>(violations);
        }

        return Collections.emptySet();
    }
}
