package com.metodi.projectapp.controllers;

import com.metodi.projectapp.controllers.dtos.TeamRequestDTO;
import com.metodi.projectapp.controllers.dtos.TeamResponseDTO;
import com.metodi.projectapp.entities.Team;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.services.AuthenticationService;
import com.metodi.projectapp.services.impl.JWTTokenService;
import com.metodi.projectapp.services.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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

@WebMvcTest(TeamController.class)
@WithMockUser(roles = "ADMIN")
class TeamControllerTest {

    private static final String TEAM_TITLE = "Team 1";
    private static final long TEAM_ID = 1;

    private static final String LOGGED_USER = "admin,adminpass";

    private static final String CREATE_TEAM_JSON = "{\"title\":\"Team 1\"}";

    private static final String EDIT_TEAM_JSON = "{\"id\":\"1\",\"title\":\"Team 1\",\"editorID\":\"0\"}";

    private static final String GET_BY_ID_TEAM_JSON = "{\"id\":1,\"title\":\"Team 1\",\"users\":null,\"projects\":null," +
            "\"dateCreated\":null,\"dateEdited\":null,\"creatorID\":0,\"editorID\":0}";

    private User loggedUser;

    private Team testTeam;

    @MockBean
    private TeamService teamService;

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
    @DisplayName("getAll() should return json array of all existing teams")
    void getAll() throws Exception {
        TeamResponseDTO teamResponseDTO = createTeamResponseDTO();
        List<TeamResponseDTO> teams = Collections.singletonList(teamResponseDTO);

        when(teamService.getAll())
                .thenReturn(teams);

        mockMvc.perform(get("/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(teamResponseDTO.getTitle())));
    }

    @Test
    @DisplayName("createTeam() should return the team that was just created in json format")
    void createTeam() throws Exception {
        TeamRequestDTO teamRequestDTO = createTeamRequestDTO();

        setTestTeamParameters(teamRequestDTO);

        when(teamService.createTeam(any(), any()))
                .thenReturn(testTeam);

        mockMvc.perform(post("/teams")
                        .header("Authorization", LOGGED_USER)
                        .content(CREATE_TEAM_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(CREATE_TEAM_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(teamService)
                .createTeam(any(), any());
    }

    @Test
    @DisplayName("editTeam() should return status 200 after successfully editing a team")
    void editTeam() throws Exception {
        TeamResponseDTO teamResponseDTO = createTeamResponseDTO();

        setTestTeamParameters(teamResponseDTO);

        when(teamService.updateTeam(TEAM_ID, teamResponseDTO, loggedUser))
                .thenReturn(testTeam);

        mockMvc.perform(put("/teams/{id}", TEAM_ID)
                        .header("Authorization", LOGGED_USER)
                        .content(EDIT_TEAM_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(teamService)
                .updateTeam(any(Long.class), any(), any());
    }

    @Test
    @DisplayName("getById() should return team in json format by specified id")
    void getById() throws Exception {
        TeamResponseDTO teamResponseDTO = createTeamResponseDTO();

        when(teamService.getById(TEAM_ID))
                .thenReturn(teamResponseDTO);

        mockMvc.perform(get("/teams/{id}", TEAM_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(GET_BY_ID_TEAM_JSON));

        verify(teamService)
                .getById(TEAM_ID);
    }

    @Test
    @DisplayName("deleteById() should delete team by specified id")
    void deleteById() throws Exception {
        mockMvc.perform(delete("/teams/{id}", TEAM_ID)
                        .header("Authorization", LOGGED_USER))
                        .andExpect(status().isOk());

        verify(teamService)
                .deleteById(TEAM_ID);
    }

    private void setTestTeamParameters(TeamRequestDTO teamRequestDTO) {
        testTeam = new Team();
        testTeam.setTitle(teamRequestDTO.getTitle());
    }

    private void setTestTeamParameters(TeamResponseDTO teamResponseDTO) {
        testTeam = new Team();
        testTeam.setId(teamResponseDTO.getId());
        testTeam.setTitle(teamResponseDTO.getTitle());
        testTeam.setEditorID(loggedUser.getId());
    }

    private TeamRequestDTO createTeamRequestDTO() {
        TeamRequestDTO teamRequestDTO = new TeamRequestDTO();
        teamRequestDTO.setTitle(TEAM_TITLE);

        return teamRequestDTO;
    }

    private TeamResponseDTO createTeamResponseDTO() {
        TeamResponseDTO teamResponseDTO = new TeamResponseDTO();
        teamResponseDTO.setId(TEAM_ID);
        teamResponseDTO.setTitle(TEAM_TITLE);

        return teamResponseDTO;
    }
}