package com.metodi.projectapp.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundTaskByIdException extends RuntimeException {

    public NotFoundTaskByIdException(String message) {
        super(message);
    }
}
