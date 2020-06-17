package com.metodi.projectapp.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class NoUserAssignAuthorizationPrivilege extends RuntimeException {

    public NoUserAssignAuthorizationPrivilege(String message) {
        super(message);
    }
}
