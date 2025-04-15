package com.biit.profile.rest.exceptions;

import com.biit.factmanager.logger.FactManagerLogger;
import com.biit.kafka.exceptions.InvalidEventException;
import com.biit.profile.core.exceptions.CandidateNotFoundException;
import com.biit.profile.core.exceptions.CommentTooLongException;
import com.biit.profile.core.exceptions.InvalidFormException;
import com.biit.profile.core.exceptions.ProfileNotFoundException;
import com.biit.profile.logger.ProfileLogger;
import com.biit.profile.persistence.entities.ProfileCandidateComment;
import com.biit.profile.persistence.entities.exceptions.InvalidProfileValueException;
import com.biit.server.exceptions.ErrorResponse;
import com.biit.server.exceptions.NotFoundException;
import com.biit.server.exceptions.ServerExceptionControllerAdvice;
import com.biit.usermanager.client.exceptions.ElementNotFoundException;
import com.biit.usermanager.client.exceptions.InvalidConfigurationException;
import com.biit.usermanager.client.exceptions.InvalidValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProfileExceptionControllerAdvice extends ServerExceptionControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> notFoundException(Exception ex) {
        ProfileLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse("NOT_FOUND", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidProfileValueException.class)
    public ResponseEntity<Object> invalidProfileValueException(Exception ex) {
        ProfileLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse("Invalid Profile payload.", "malformed_profile", ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<Object> profileNotFoundException(Exception ex) {
        ProfileLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "profile_not_found", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CandidateNotFoundException.class)
    public ResponseEntity<Object> candidateNotFoundException(Exception ex) {
        ProfileLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "candidate_not_found", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommentTooLongException.class)
    public ResponseEntity<Object> commentTooLongException(Exception ex) {
        ProfileLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse("Comment length exceeds the limit of '"
                + ProfileCandidateComment.COMMENT_LENGTH + "' bytes", "comment_too_long", ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFormException.class)
    public ResponseEntity<Object> invalidFormException(Exception ex) {
        ProfileLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "invalid_form", ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEventException.class)
    public ResponseEntity<Object> invalidEventException(Exception ex) {
        ProfileLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "cannot_connect_to_kafka", ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<Object> elementNotFoundException(Exception ex) {
        ProfileLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "not_found", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidConfigurationException.class)
    public ResponseEntity<Object> invalidConfigurationException(Exception ex) {
        ProfileLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "invalid_configuration_exception", ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<Object> invalidValueException(Exception ex) {
        ProfileLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "invalid_parameter", ex), HttpStatus.BAD_REQUEST);
    }
}
