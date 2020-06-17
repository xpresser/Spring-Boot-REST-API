package com.metodi.projectapp.controllers;

import com.metodi.projectapp.controllers.dtos.ProjectRequestDTO;
import com.metodi.projectapp.controllers.dtos.ProjectResponseDTO;
import com.metodi.projectapp.entities.Project;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.services.AuthenticationService;
import com.metodi.projectapp.services.impl.JWTTokenService;
import com.metodi.projectapp.services.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    private static final String LOGGED_USER = "admin,adminpass";
    private static final int PROJECT_ID = 1;

    private static final String CREATE_PROJECT_JSON = "{\"title\":\"midterm project\"," +
            "\"description\":\"my second project\"}";

    private static final String EDIT_PROJECT_JSON = "{\"title\":\"midterm project\"," +
            "\"description:\":\"my second project\"}";

    private static final String GET_BY_ID_PROJECT_JSON = "{\"id\":1,\"title\":\"todo application\"," +
            "\"description\":\"my first project\",\"tasks\":null,\"teams\":null,\"dateCreated\":null," +
            "\"dateEdited\":null,\"creatorID\":0,\"editorID\":0}";

    private User loggedUser;

    private Project testProject;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JWTTokenService jwtTokenService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.loggedUser = new User();
        when(authenticationService.getLoggedUser())
                .thenReturn(loggedUser);
    }

    @Test
    @DisplayName("getAll() should return json array of all existing projects")
    void getAll() throws Exception {
        ProjectResponseDTO projectResponseDTO = createProjectResponseDTO();
        List<ProjectResponseDTO> projects = Collections.singletonList(projectResponseDTO);

        when(projectService.getAll(any()))
                .thenReturn(projects);

        mockMvc.perform(get("/projects")
                            .header("Authorization", LOGGED_USER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(projectResponseDTO.getTitle())));

        verify(projectService)
                .getAll(any());
    }

    @Test
    @DisplayName("createProject() should return the project that was just created")
    void createProject() throws Exception {
        ProjectRequestDTO projectRequestDTO = createProjectRequestDTO();

        setTestProjectParameters(projectRequestDTO);

        when(projectService.createProject(any(), any()))
                .thenReturn(testProject);

        mockMvc.perform(post("/projects")
                        .header("Authorization", LOGGED_USER)
                        .content(CREATE_PROJECT_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(CREATE_PROJECT_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(projectService)
                .createProject(any(), any());
    }

    @Test
    @DisplayName("editProject() should return status 200 after successfully editing a project")
    void editProject() throws Exception {
        ProjectResponseDTO projectResponseDTO = createProjectResponseDTO();

        setTestProjectParameters(projectResponseDTO);

        when(projectService.updateProject(PROJECT_ID, projectResponseDTO, loggedUser))
                .thenReturn(testProject);

        mockMvc.perform(put("/projects/{id}", PROJECT_ID)
                        .header("Authorization", LOGGED_USER)
                        .content(EDIT_PROJECT_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(projectService)
                .updateProject(any(Long.class), any(), any());
    }

    @Test
    @DisplayName("getById() should return project in json format by specified id")
    void getById() throws Exception {
        ProjectResponseDTO projectResponseDTO = createProjectResponseDTO();

        when(projectService.getById(PROJECT_ID))
                .thenReturn(projectResponseDTO);

        mockMvc.perform(get("/projects/{id}", PROJECT_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(GET_BY_ID_PROJECT_JSON));

        verify(projectService)
                .getById(PROJECT_ID);
    }

    @Test
    @DisplayName("deleteById() should delete a project by specified id")
    void deleteById() throws Exception {
        mockMvc.perform(delete("/projects/{id}", PROJECT_ID)
                        .header("Authorization", LOGGED_USER))
                        .andExpect(status().isOk());

        verify(projectService)
                .deleteById(PROJECT_ID);
    }

    private void setTestProjectParameters(ProjectRequestDTO projectRequestDTO) {
        testProject = new Project();
        testProject.setTitle(projectRequestDTO.getTitle());
        testProject.setDescription(projectRequestDTO.getDescription());
    }

    private void setTestProjectParameters(ProjectResponseDTO projectResponseDTO) {
        testProject = new Project();
        testProject.setTitle(projectResponseDTO.getTitle());
        testProject.setDescription(projectResponseDTO.getDescription());
        testProject.setEditorID(projectResponseDTO.getEditorID());
    }

    private ProjectResponseDTO createProjectResponseDTO() {
        ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO();
        projectResponseDTO.setId(PROJECT_ID);
        projectResponseDTO.setTitle("todo application");
        projectResponseDTO.setDescription("my first project");

        return projectResponseDTO;
    }

    private ProjectRequestDTO createProjectRequestDTO() {
        ProjectRequestDTO projectRequestDTO = new ProjectRequestDTO();
        projectRequestDTO.setTitle("midterm project");
        projectRequestDTO.setDescription("my second project");

        return projectRequestDTO;
    }
}