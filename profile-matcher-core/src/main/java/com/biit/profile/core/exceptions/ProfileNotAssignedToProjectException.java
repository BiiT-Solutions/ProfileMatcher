package com.biit.profile.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProfileNotAssignedToProjectException extends NotFoundException {


    @Serial
    private static final long serialVersionUID = -4349738731750086190L;

    public ProfileNotAssignedToProjectException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public ProfileNotAssignedToProjectException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public ProfileNotAssignedToProjectException(Class<?> clazz) {
        this(clazz, "Profile not assigned");
    }

    public ProfileNotAssignedToProjectException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
