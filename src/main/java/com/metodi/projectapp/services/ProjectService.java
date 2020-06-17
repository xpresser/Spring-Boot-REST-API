package com.metodi.projectapp.services;

import com.metodi.projectapp.entities.Project;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.controllers.dtos.ProjectRequestDTO;
import com.metodi.projectapp.controllers.dtos.ProjectResponseDTO;

import java.util.List;

public interface ProjectService {

    List<ProjectResponseDTO> getAll(User loggedUser);

    Project createProject(ProjectRequestDTO projectRequestDTO, User loggedUser);

    Project updateProject(long projectID, ProjectResponseDTO projectResponseDTO, User loggedUser);

    void deleteById(long projectID);

    ProjectResponseDTO getById(long projectID);

    void assignTeamById(long projectID, long teamID, User loggedUser);
}
