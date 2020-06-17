package com.metodi.projectapp.services.impl;

import com.metodi.projectapp.controllers.dtos.TeamRequestDTO;
import com.metodi.projectapp.controllers.dtos.TeamResponseDTO;
import com.metodi.projectapp.entities.Project;
import com.metodi.projectapp.entities.Team;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.repositories.TeamRepository;
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
class TeamServiceImplTest {

    private static final long USER_ID = 10;
    private static final long TEAM_ID = 1;
    private static final String TEAM_TITLE = "Team 1";
    private static final String EDITED_TEAM_TITLE = "edited team title";

    private User loggedUser;

    private Team testTeam;

    @MockBean
    private TeamRepository teamRepository;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private TeamServiceImpl teamService;

    @BeforeEach
    void setUp() {
        createLoggedUser();

        when(authenticationService.getLoggedUser())
                .thenReturn(loggedUser);
        this.testTeam = createTestTeam();
    }

    @Test
    @DisplayName("Should return json array of all Teams")
    void getAll() {
        List<Team> teams = Collections.singletonList(testTeam);

        when(teamRepository.findAll())
                .thenReturn(teams);

        List<TeamResponseDTO> list = teams
                .stream().map(Team::getResponseDTO)
                .sorted(Comparator.comparing(TeamResponseDTO::getTitle))
                .collect(Collectors.toList());

        List<TeamResponseDTO> actualTeamList = teamService.getAll();

        assertAll(
                () -> assertNotNull(actualTeamList),
                () -> assertEquals(list, actualTeamList)
        );

        verify(teamRepository)
                .findAll();
    }

    @Test
    @DisplayName("Should created Team successfully")
    void createTeam() {
        TeamRequestDTO teamRequestDTO = createTeamRequestDTO();

        testTeam.setTeamRequestDtoParameters(teamRequestDTO, loggedUser, testTeam);

        when(teamRepository.save(any()))
                .thenReturn(testTeam);

        Team actualTeam = this.teamService.createTeam(teamRequestDTO, loggedUser);

        assertNotNull(actualTeam);

        verify(teamRepository).save(any());
    }

    @Test
    @DisplayName("Should edit Team successfully")
    void editTeam() {
        TeamResponseDTO teamResponseDTO = createTeamResponseDTO();

        testTeam.setTeamResponseDtoParameters(teamResponseDTO, loggedUser, testTeam);

        when(teamRepository.findById(TEAM_ID))
                .thenReturn(java.util.Optional.ofNullable(testTeam));

        when(teamRepository.save(any(Team.class)))
                .thenReturn(testTeam);

        Team actualTeam = teamService.updateTeam(TEAM_ID, teamResponseDTO, loggedUser);

        assertAll(
                () -> assertNotNull(actualTeam),
                () -> assertEquals(testTeam, actualTeam)
        );

        verify(teamRepository)
                .save(any(Team.class));
        verify(teamRepository)
                .findById(anyLong());
    }

    @Test
    @DisplayName("Should delete Team by given id")
    void deleteTeam() {
        when(teamRepository.findById(TEAM_ID))
                .thenReturn(java.util.Optional.ofNullable(testTeam));

        this.teamService.deleteById(TEAM_ID);

        verify(teamRepository)
                .deleteById(anyLong());
    }

    @Test
    @DisplayName("Should get Team by specified id")
    void getTeamById() {
        when(teamRepository.findById(TEAM_ID))
                .thenReturn(java.util.Optional.ofNullable(testTeam));

        TeamResponseDTO actualTeam = this.teamService.getById(TEAM_ID);

        assertNotNull(actualTeam);

        verify(teamRepository)
                .findById(anyLong());
    }

    private TeamRequestDTO createTeamRequestDTO() {
        TeamRequestDTO teamRequestDTO = new TeamRequestDTO();
        teamRequestDTO.setTitle(TEAM_TITLE);

        return teamRequestDTO;
    }

    private TeamResponseDTO createTeamResponseDTO() {
        TeamResponseDTO teamResponseDTO = new TeamResponseDTO();
        teamResponseDTO.setTitle(EDITED_TEAM_TITLE);
        teamResponseDTO.setEditorID(USER_ID);

        return teamResponseDTO;
    }

    private Team createTestTeam() {
        testTeam = new Team();
        testTeam.setId(TEAM_ID);
        testTeam.setTitle(TEAM_TITLE);
        testTeam.setUsers(Set.of(new User()));
        testTeam.setProjects(Set.of(new Project()));
        testTeam.setCreatorID(loggedUser.getId());
        testTeam.setEditorID(loggedUser.getId());

        return testTeam;
    }

    private void createLoggedUser() {
        loggedUser = new User();
        loggedUser.setId(USER_ID);
        loggedUser.setAdmin(true);
    }
}