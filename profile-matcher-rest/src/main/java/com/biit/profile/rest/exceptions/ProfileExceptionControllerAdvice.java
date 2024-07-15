package com.biit.profile.rest.exceptions;

import com.biit.profile.core.exceptions.CandidateNotFoundException;
import com.biit.profile.core.exceptions.CommentTooLongException;
import com.biit.profile.core.exceptions.ProfileNotFoundException;
import com.biit.profile.persistence.entities.ProfileCandidateComment;
import com.biit.profile.persistence.entities.exceptions.InvalidProfileValueException;
import com.biit.server.exceptions.NotFoundException;
import com.biit.server.exceptions.ServerExceptionControllerAdvice;
import com.biit.server.logger.RestServerExceptionLogger;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProfileExceptionControllerAdvice extends ServerExceptionControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> notFoundException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage("NOT_FOUND", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidProfileValueException.class)
    public ResponseEntity<Object> invalidProfileValueException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage("Invalid Profile payload.", ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<Object> profileNotFoundException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage(), ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CandidateNotFoundException.class)
    public ResponseEntity<Object> candidateNotFoundException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage(), ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommentTooLongException.class)
    public ResponseEntity<Object> commentTooLongException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage("Comment length exceeds the limit of '"
                + ProfileCandidateComment.COMMENT_LENGTH + "' bytes", ex), HttpStatus.BAD_REQUEST);
    }
}
