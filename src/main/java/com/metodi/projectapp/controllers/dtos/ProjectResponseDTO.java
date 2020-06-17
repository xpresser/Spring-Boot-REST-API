package com.metodi.projectapp.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.metodi.projectapp.entities.BaseData;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@JsonPropertyOrder(value = {"id", "title", "description", "tasks", "teams"})
@Getter @Setter @EqualsAndHashCode(callSuper = false)
public class ProjectResponseDTO extends BaseData {

    private long id;

    private String title;

    private String description;

    private Set<Long> tasks;

    private Set<String> teams;
}
