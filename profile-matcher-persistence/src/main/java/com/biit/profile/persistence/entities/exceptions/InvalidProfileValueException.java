package com.biit.profile.persistence.entities.exceptions;

import java.io.Serial;

public class InvalidProfileValueException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -8262997974818290107L;

    public InvalidProfileValueException(Throwable e) {
        super(e);
    }

    public InvalidProfileValueException(String message) {
        super(message);
    }
}
