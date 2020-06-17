package com.metodi.projectapp.controllers;

import com.metodi.projectapp.controllers.dtos.TaskRequestDTO;
import com.metodi.projectapp.controllers.dtos.TaskResponseDTO;
import com.metodi.projectapp.entities.Task;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.services.AuthenticationService;
import com.metodi.projectapp.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class TaskController {

    private final TaskService taskService;

    private final AuthenticationService authenticationService;

    @Autowired
    public TaskController(TaskService taskService,
                          AuthenticationService authenticationService) {
        this.taskService = taskService;
        this.authenticationService = authenticationService;
    }

    @GetMapping(value = "/{projectID}/tasks")
    public List<Task> getAll(@PathVariable(value = "projectID") long projectID) {

        User loggedUser = authenticationService.getLoggedUser();

        return this.taskService.getAll(projectID, loggedUser);
    }

    @PostMapping(value = "/{projectID}/tasks")
    public Task create(@PathVariable(value = "projectID") long projectID,
                       @RequestBody TaskRequestDTO taskRequestDTO) {

        User loggedUser = authenticationService.getLoggedUser();

        return this.taskService.createTask(projectID, taskRequestDTO, loggedUser);
    }

    @PutMapping(value = "/{projectID}/tasks/{taskID}")
    public Task update(@PathVariable(value = "projectID") long projectID,
                       @PathVariable(value = "taskID") long taskID,
                       @RequestBody TaskResponseDTO taskResponseDTO) {

        User loggedUser = authenticationService.getLoggedUser();

        return this.taskService.updateTask(projectID, taskID, taskResponseDTO, loggedUser);
    }

    @DeleteMapping(value = "/{projectID}/tasks/{taskID}")
    public void deleteById(@PathVariable(value = "projectID") long projectID,
                           @PathVariable(value = "taskID") long taskID) {

        this.taskService.deleteById(projectID, taskID);
    }

    @GetMapping(value = "/{projectID}/tasks/{taskID}")
    public Task getById(@PathVariable(value = "projectID") long projectID,
                        @PathVariable(value = "taskID") long taskID) {

        return this.taskService.getById(projectID, taskID);
    }
}
