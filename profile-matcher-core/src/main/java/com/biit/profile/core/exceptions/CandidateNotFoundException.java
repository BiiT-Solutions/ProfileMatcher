package com.biit.profile.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CandidateNotFoundException extends NotFoundException {
    private static final long serialVersionUID = 7232994111675694372L;

    public CandidateNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public CandidateNotFoundException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public CandidateNotFoundException(Class<?> clazz) {
        this(clazz, "Candidate not found");
    }

    public CandidateNotFoundException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
