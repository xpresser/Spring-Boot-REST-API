package com.metodi.projectapp.controllers;

import com.metodi.projectapp.controllers.dtos.TeamRequestDTO;
import com.metodi.projectapp.controllers.dtos.TeamResponseDTO;
import com.metodi.projectapp.entities.Team;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.services.AuthenticationService;
import com.metodi.projectapp.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
@PreAuthorize("hasRole('ADMIN')")
public class TeamController {

    private final TeamService teamService;

    private final AuthenticationService authenticationService;

    @Autowired
    public TeamController(TeamService teamService,
                          AuthenticationService authenticationService) {
        this.teamService = teamService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public List<TeamResponseDTO> getAll() {
        return this.teamService.getAll();
    }

    @PostMapping
    public Team create(@RequestBody TeamRequestDTO teamRequestDTO) {

        User loggedUser = authenticationService.getLoggedUser();

        return this.teamService.createTeam(teamRequestDTO, loggedUser);
    }

    @PutMapping(value = "/{id}")
    public Team update(@PathVariable(value = "id") long teamID,
                       @RequestBody TeamResponseDTO teamResponseDTO) {

        User loggedUser = authenticationService.getLoggedUser();

        return this.teamService.updateTeam(teamID, teamResponseDTO, loggedUser);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable(value = "id") long teamID) {

        this.teamService.deleteById(teamID);
    }

    @GetMapping("/{id}")
    public TeamResponseDTO getById(@PathVariable(value = "id") long teamID) {
        return this.teamService.getById(teamID);
    }

    @PutMapping("/{teamID}/users/{userID}")
    public void assignUserById(@PathVariable(value = "teamID") long teamID,
                               @PathVariable(value = "userID") long userID) {

        this.teamService.assignUserById(teamID, userID);
    }
}
