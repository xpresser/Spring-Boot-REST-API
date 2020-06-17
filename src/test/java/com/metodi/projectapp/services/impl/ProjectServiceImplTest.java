package com.metodi.projectapp.services.impl;

import com.metodi.projectapp.controllers.dtos.ProjectRequestDTO;
import com.metodi.projectapp.controllers.dtos.ProjectResponseDTO;
import com.metodi.projectapp.entities.Project;
import com.metodi.projectapp.entities.Task;
import com.metodi.projectapp.entities.Team;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.repositories.ProjectRepository;
import com.metodi.projectapp.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProjectServiceImplTest {

    private static final long USER_ID = 10;

    private static final long PROJECT_ID = 2;
    private static final String PROJECT_TITLE = "project title";
    private static final String PROJECT_DESCRIPTION = "project description";

    private static final String EDITED_PROJECT_TITLE = "edited project title";
    private static final String EDITED_PROJECT_DESCRIPTION = "edited project description";

    private User loggedUser;

    private Project testProject;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        createLoggedUser();

        when(authenticationService.getLoggedUser())
                .thenReturn(loggedUser);
        this.testProject = createTestProject();
    }

    @Test
    @DisplayName("Should return json array of all projects")
    void getAll() {
        List<Project> projects = Collections.singletonList(testProject);

        when(projectRepository.findAll())
                .thenReturn(projects);

        List<ProjectResponseDTO> list = projects
                .stream()
                .filter(project -> project.getCreatorID() == loggedUser.getId() ||
                        project.getTeams()
                                .stream().anyMatch(team -> team.getUsers()
                                .stream().anyMatch(user -> user.getId() == loggedUser.getId())))
                .map(Project::getResponseDTO)
                .sorted(Comparator.comparing(ProjectResponseDTO::getTitle))
                .collect(Collectors.toList());

        List<ProjectResponseDTO> actualProjectList = projectService.getAll(loggedUser);

        assertAll(
                () -> assertNotNull(actualProjectList),
                () -> assertEquals(list, actualProjectList)
        );

        verify(projectRepository)
                .findAll();
    }

    @Test
    @DisplayName("Should create Project successfully")
    void createProject() {
        ProjectRequestDTO projectRequestDTO = createProjectRequestDTO();

        testProject.setProjectRequestDtoParameters(projectRequestDTO, loggedUser, testProject);

        when(projectRepository.save(any()))
                .thenReturn(testProject);

        Project actualProject = this.projectService.createProject(projectRequestDTO, loggedUser);

        assertNotNull(actualProject);

        verify(projectRepository).save(any());
    }

    @Test
    @DisplayName("Should edit Project successfully")
    void editProject() {
        ProjectResponseDTO projectResponseDTO = createProjectResponseDTO();

        testProject.setProjectResponseDtoParameters(projectResponseDTO, loggedUser, testProject);

        when(projectRepository.findById(PROJECT_ID))
                .thenReturn(java.util.Optional.ofNullable(testProject));

        when(projectRepository.save(any(Project.class)))
                .thenReturn(testProject);

        Project actualProject = projectService.updateProject(PROJECT_ID, projectResponseDTO, loggedUser);

        assertAll(
                () -> assertNotNull(actualProject),
                () -> assertEquals(testProject, actualProject)
        );

        verify(projectRepository)
                .save(any(Project.class));

        verify(projectRepository)
                .findById(anyLong());
    }

    @Test
    @DisplayName("Should delete Project by given id")
    void deleteProject() {
        when(projectRepository.findById(PROJECT_ID))
                .thenReturn(java.util.Optional.ofNullable(testProject));

        this.projectService.deleteById(PROJECT_ID);

        verify(projectRepository)
                .deleteById(anyLong());
    }

    @Test
    @DisplayName("Should get Project by specified id")
    void getProjectById() {
        when(projectRepository.findById(PROJECT_ID))
                .thenReturn(java.util.Optional.ofNullable(testProject));

        ProjectResponseDTO actualProject = this.projectService.getById(PROJECT_ID);

        assertNotNull(actualProject);

        verify(projectRepository)
                .findById(anyLong());
    }

    private ProjectRequestDTO createProjectRequestDTO() {
        ProjectRequestDTO projectRequestDTO = new ProjectRequestDTO();
        projectRequestDTO.setTitle(PROJECT_TITLE);
        projectRequestDTO.setDescription(PROJECT_DESCRIPTION);

        return projectRequestDTO;
    }

    private ProjectResponseDTO createProjectResponseDTO() {
        ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO();
        projectResponseDTO.setTitle(EDITED_PROJECT_TITLE);
        projectResponseDTO.setDescription(EDITED_PROJECT_DESCRIPTION);

        return projectResponseDTO;
    }

    private Project createTestProject() {
        Team team = new Team();
        team.setUsers(Set.of(loggedUser));
        testProject = new Project();
        testProject.setId(PROJECT_ID);
        testProject.setTitle(PROJECT_TITLE);
        testProject.setDescription(PROJECT_DESCRIPTION);
        testProject.setTasks(Set.of(new Task()));
        testProject.setTeams(Set.of(team));

        return testProject;
    }

    private void createLoggedUser() {
        loggedUser = new User();
        loggedUser.setId(USER_ID);
        loggedUser.setAdmin(true);
    }
}