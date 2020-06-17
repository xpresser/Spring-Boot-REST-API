package com.metodi.projectapp.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "No permission to access resource")
public class NoAdminPrivilegesException extends RuntimeException {

    public NoAdminPrivilegesException(String message) {
        super(message);
    }
}
