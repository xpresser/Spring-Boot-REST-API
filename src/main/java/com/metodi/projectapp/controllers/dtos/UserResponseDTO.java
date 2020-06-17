package com.metodi.projectapp.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.metodi.projectapp.entities.BaseData;
import com.sun.istack.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@JsonPropertyOrder(value = {"id", "username", "firstName", "lastName", "isAdmin"})
@Getter @Setter @EqualsAndHashCode(callSuper = false)
public class UserResponseDTO extends BaseData {

    private long id;

    private String username;

    @NotBlank
    private String password;

    private String firstName;

    private String lastName;

    private boolean isAdmin;

    @NotNull
    private Set<String> teams;
}
