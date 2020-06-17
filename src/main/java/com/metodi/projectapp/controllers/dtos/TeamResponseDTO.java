package com.metodi.projectapp.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.metodi.projectapp.entities.BaseData;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@JsonPropertyOrder(value = {"id", "title", "users", "projects"})
@Getter @Setter @EqualsAndHashCode(callSuper = false)
public class TeamResponseDTO extends BaseData {

    private long id;

    private String title;

    private Set<Long> users;

    private Set<Long> projects;
}
