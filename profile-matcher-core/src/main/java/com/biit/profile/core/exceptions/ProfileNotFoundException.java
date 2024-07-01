package com.biit.profile.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProfileNotFoundException extends NotFoundException {
    private static final long serialVersionUID = 7132994111678894371L;

    public ProfileNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public ProfileNotFoundException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public ProfileNotFoundException(Class<?> clazz) {
        this(clazz, "Profile not found");
    }

    public ProfileNotFoundException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
