package com.metodi.projectapp.services.impl;

import com.metodi.projectapp.controllers.dtos.ProjectRequestDTO;
import com.metodi.projectapp.controllers.dtos.ProjectResponseDTO;
import com.metodi.projectapp.entities.Project;
import com.metodi.projectapp.entities.Team;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.repositories.ProjectRepository;
import com.metodi.projectapp.repositories.TeamRepository;
import com.metodi.projectapp.services.ProjectService;
import com.metodi.projectapp.services.exceptions.NoUserAssignAuthorizationPrivilege;
import com.metodi.projectapp.services.exceptions.NotFoundProjectByIdException;
import com.metodi.projectapp.services.exceptions.NotFoundTeamByIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final TeamRepository teamRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository,
                              TeamRepository teamRepository) {
        this.projectRepository = projectRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public List<ProjectResponseDTO> getAll(User loggedUser) {
        return this.projectRepository.findAll().stream()
                .filter(project -> project.getCreatorID() == loggedUser.getId() ||
                        project.getTeams()
                                .stream().anyMatch(team -> team.getUsers()
                                .stream().anyMatch(user -> user.getId() == loggedUser.getId())))
                .map(Project::getResponseDTO)
                .sorted(Comparator.comparing(ProjectResponseDTO::getId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Project createProject(ProjectRequestDTO projectRequestDTO, User loggedUser) {
        Project project = new Project();
        project.setProjectRequestDtoParameters(projectRequestDTO, loggedUser, project);

        return this.projectRepository.save(project);
    }

    @Override
    @Transactional
    public Project updateProject(long projectID, ProjectResponseDTO projectResponseDTO, User loggedUser) {
        Project project = projectRepository.findById(projectID)
                .orElseThrow(() -> new NotFoundProjectByIdException("Project not found with id = " + projectID));

        project.setProjectResponseDtoParameters(projectResponseDTO, loggedUser, project);

        return this.projectRepository.save(project);
    }

    @Override
    public void deleteById(long projectID) {
        checkIfProjectIdExists(projectID);

        this.projectRepository.deleteById(projectID);
    }

    @Override
    public ProjectResponseDTO getById(long projectID) {
        return this.projectRepository.findById(projectID).map(Project::getResponseDTO)
                .orElseThrow(() -> new NotFoundProjectByIdException("Project not found with id = " + projectID));
    }

    @Override
    public void assignTeamById(long projectID, long teamID, User loggedUser) {
        checkIfProjectIdExists(projectID);

        checkIfTeamIdExists(teamID);

        checkIfTeamHasUserId(teamID, loggedUser);

        Project project = this.projectRepository.getOne(projectID);
        Team team = this.teamRepository.getOne(teamID);

        project.assignTeamToProject(team);

        this.projectRepository.save(project);
    }

    private void checkIfTeamHasUserId(long teamID, User loggedUser) {
        Team team = this.teamRepository.getOne(teamID);
        if (team.getUsers().stream().noneMatch(user -> user.getId() == loggedUser.getId())) {
            throw new NoUserAssignAuthorizationPrivilege("User does not belong to Team with id = " + teamID);
        }
    }

    private void checkIfProjectIdExists(long projectID) {
        if (this.projectRepository.findById(projectID).isEmpty()) {
            throw new NotFoundProjectByIdException("Project not found with id = " + projectID);
        }
    }

    private void checkIfTeamIdExists(long teamID) {
        if (this.teamRepository.findById(teamID).isEmpty()) {
            throw new NotFoundTeamByIdException("Team not found with id = " + teamID);
        }
    }
}
