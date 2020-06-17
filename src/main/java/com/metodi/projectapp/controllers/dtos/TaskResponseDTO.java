package com.metodi.projectapp.controllers.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TaskResponseDTO {

    private String title;

    private String description;

    private boolean status;

    private long projectID;

    private long assigneeID;
}
