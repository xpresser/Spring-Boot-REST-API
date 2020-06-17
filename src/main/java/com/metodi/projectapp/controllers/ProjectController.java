package com.metodi.projectapp.controllers;

import com.metodi.projectapp.controllers.dtos.ProjectRequestDTO;
import com.metodi.projectapp.controllers.dtos.ProjectResponseDTO;
import com.metodi.projectapp.entities.Project;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.services.AuthenticationService;
import com.metodi.projectapp.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    private final AuthenticationService authenticationService;

    @Autowired
    public ProjectController(ProjectService projectService,
                             AuthenticationService authenticationService) {
        this.projectService = projectService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public List<ProjectResponseDTO> getAll() {

        User loggedUser = authenticationService.getLoggedUser();

        return this.projectService.getAll(loggedUser);
    }

    @PostMapping
    public Project create(@RequestBody ProjectRequestDTO projectRequestDTO) {

        User loggedUser = authenticationService.getLoggedUser();

        return this.projectService.createProject(projectRequestDTO, loggedUser);
    }

    @PutMapping(value = "/{id}")
    public Project update(@PathVariable(value = "id") long projectID,
                          @RequestBody ProjectResponseDTO projectResponseDTO) {

        User loggedUser = authenticationService.getLoggedUser();

        return this.projectService.updateProject(projectID, projectResponseDTO, loggedUser);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable(value = "id") long projectID) {

        this.projectService.deleteById(projectID);
    }

    @GetMapping("/{id}")
    public ProjectResponseDTO getById(@PathVariable(value = "id") long projectID) {
        return this.projectService.getById(projectID);
    }

    @PutMapping("/{projectID}/teams/{teamID}")
    public void assignTeamById(@PathVariable(value = "projectID") long projectID,
                               @PathVariable(value = "teamID") long teamID) {

        User loggedUser = authenticationService.getLoggedUser();

        this.projectService.assignTeamById(projectID, teamID, loggedUser);
    }
}
