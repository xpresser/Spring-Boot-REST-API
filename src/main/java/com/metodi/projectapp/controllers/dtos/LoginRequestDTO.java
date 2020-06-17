package com.metodi.projectapp.controllers.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequestDTO {

    private String username;

    private String password;
}
