package com.metodi.projectapp.services;

import com.metodi.projectapp.entities.Team;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.controllers.dtos.TeamRequestDTO;
import com.metodi.projectapp.controllers.dtos.TeamResponseDTO;

import java.util.List;

public interface TeamService {

    List<TeamResponseDTO> getAll();

    Team createTeam(TeamRequestDTO teamRequestDTO, User loggedUser);

    Team updateTeam(long teamID, TeamResponseDTO teamResponseDTO, User loggedUser);

    void deleteById(long teamID);

    TeamResponseDTO getById(long teamID);

    void assignUserById(long teamID, long userID);
}
