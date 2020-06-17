package com.metodi.projectapp.services;

import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.entities.WorkLog;
import com.metodi.projectapp.controllers.dtos.WorkLogRequestDTO;
import com.metodi.projectapp.controllers.dtos.WorkLogResponseDTO;

import java.util.List;

public interface WorkLogService {

    List<WorkLog> getAll(long projectID, long taskID);

    WorkLog createWorkLog(long projectID, long taskID, WorkLogRequestDTO workLogRequestDTO, User loggedUser);

    WorkLog updateWorkLog(long projectID, long taskID, long workLogID, WorkLogResponseDTO workLogResponseDTO, User loggedUser);

    void deleteById(long projectID, long taskID, long workLogID);

    WorkLog getById(long projectID, long taskID, long workLogID);
}
