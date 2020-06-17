package com.metodi.projectapp.controllers.dtos;

import com.metodi.projectapp.entities.Team;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
public class UserRequestDTO {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private boolean isAdmin;

    private Set<Team> teams;
}
