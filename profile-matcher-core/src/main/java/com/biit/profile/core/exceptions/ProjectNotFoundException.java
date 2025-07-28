package com.biit.profile.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProjectNotFoundException extends NotFoundException {

    @Serial
    private static final long serialVersionUID = 3628314678717091636L;

    public ProjectNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public ProjectNotFoundException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public ProjectNotFoundException(Class<?> clazz) {
        this(clazz, "Profile not found");
    }

    public ProjectNotFoundException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
