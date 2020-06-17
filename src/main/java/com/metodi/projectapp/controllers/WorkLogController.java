package com.metodi.projectapp.controllers;

import com.metodi.projectapp.controllers.dtos.WorkLogRequestDTO;
import com.metodi.projectapp.controllers.dtos.WorkLogResponseDTO;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.entities.WorkLog;
import com.metodi.projectapp.services.AuthenticationService;
import com.metodi.projectapp.services.WorkLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class WorkLogController {

    private final WorkLogService workLogService;

    private final AuthenticationService authenticationService;

    @Autowired
    public WorkLogController(WorkLogService workLogService,
                             AuthenticationService authenticationService) {
        this.workLogService = workLogService;
        this.authenticationService = authenticationService;
    }

    @GetMapping(value = "/{projectID}/tasks/{taskID}/worklogs")
    public List<WorkLog> getAll(@PathVariable(value = "projectID") long projectID,
                                @PathVariable(value = "taskID") long taskID) {

        return this.workLogService.getAll(projectID, taskID);
    }

    @PostMapping(value = "/{projectID}/tasks/{taskID}/worklogs")
    public WorkLog create(@PathVariable(value = "projectID") long projectID,
                          @PathVariable(value = "taskID") long taskID,
                          @RequestBody WorkLogRequestDTO workLogRequestDTO) {

        User loggedUser = authenticationService.getLoggedUser();

        return this.workLogService.createWorkLog(projectID, taskID, workLogRequestDTO, loggedUser);
    }

    @PutMapping(value = "/{projectID}/tasks/{taskID}/worklogs/{worklogID}")
    public WorkLog update(@PathVariable(value = "projectID") long projectID,
                          @PathVariable(value = "taskID") long taskID,
                          @PathVariable(value = "worklogID") long worklogID,
                          @RequestBody WorkLogResponseDTO workLogResponseDTO) {

        User loggedUser = authenticationService.getLoggedUser();

        return this.workLogService.updateWorkLog(projectID, taskID, worklogID, workLogResponseDTO, loggedUser);
    }

    @DeleteMapping(value = "/{projectID}/tasks/{taskID}/worklogs/{worklogID}")
    public void deleteById(@PathVariable(value = "projectID") long projectID,
                           @PathVariable(value = "taskID") long taskID,
                           @PathVariable(value = "worklogID") long worklogID) {

        this.workLogService.deleteById(projectID, taskID, worklogID);
    }

    @GetMapping(value = "/{projectID}/tasks/{taskID}/worklogs/{worklogID}")
    public WorkLog getById(@PathVariable(value = "projectID") long projectID,
                           @PathVariable(value = "taskID") long taskID,
                           @PathVariable(value = "worklogID") long worklogID) {

        return this.workLogService.getById(projectID, taskID, worklogID);
    }
}
