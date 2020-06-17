package com.metodi.projectapp.controllers.dtos;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter @Setter
public class WorkLogRequestDTO {

    private long taskID;

    private long userID;

    private int hoursSpent;

    private Date dateWorked;
}
