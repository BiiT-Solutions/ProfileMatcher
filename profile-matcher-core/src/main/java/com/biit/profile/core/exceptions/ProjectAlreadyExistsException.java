package com.biit.profile.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.logger.LoggedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProjectAlreadyExistsException extends LoggedException {

    @Serial
    private static final long serialVersionUID = 246440323097421589L;

    public ProjectAlreadyExistsException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public ProjectAlreadyExistsException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.INFO, HttpStatus.BAD_REQUEST);
    }

    public ProjectAlreadyExistsException(Class<?> clazz) {
        this(clazz, "Project already exists!");
    }

    public ProjectAlreadyExistsException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
