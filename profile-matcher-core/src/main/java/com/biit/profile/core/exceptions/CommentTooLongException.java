package com.biit.profile.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CommentTooLongException extends NotFoundException {
    private static final long serialVersionUID = 7232994218435694372L;

    public CommentTooLongException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public CommentTooLongException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public CommentTooLongException(Class<?> clazz) {
        this(clazz, "Candidate not found");
    }

    public CommentTooLongException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
