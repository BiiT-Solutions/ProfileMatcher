package com.biit.profile.persistence.entities.exceptions;

public class InvalidProfileValueException extends RuntimeException {
    public InvalidProfileValueException(Throwable e) {
        super(e);
    }
}
