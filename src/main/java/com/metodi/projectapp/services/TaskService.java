package com.metodi.projectapp.services;

import com.metodi.projectapp.entities.Task;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.controllers.dtos.TaskRequestDTO;
import com.metodi.projectapp.controllers.dtos.TaskResponseDTO;

import java.util.List;

public interface TaskService {

    List<Task> getAll(long projectID, User loggedUser);

    Task createTask(long projectID, TaskRequestDTO taskRequestDTO, User loggedUser);

    Task updateTask(long projectID, long taskID, TaskResponseDTO taskResponseDTO, User loggedUser);

    void deleteById(long projectID, long taskID);

    Task getById(long projectID, long taskID);
}
