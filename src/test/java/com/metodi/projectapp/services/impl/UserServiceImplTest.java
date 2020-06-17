package com.metodi.projectapp.services.impl;

import com.metodi.projectapp.controllers.dtos.UserRequestDTO;
import com.metodi.projectapp.controllers.dtos.UserResponseDTO;
import com.metodi.projectapp.entities.Team;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.repositories.UserRepository;
import com.metodi.projectapp.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    private static final long USER_ID = 1;
    private static final String USER_USERNAME = "bobby";
    private static final String USER_PASSWORD = "123";
    private static final String USER_FIRST_NAME = "Bob";
    private static final String USER_LAST_NAME = "Smith";

    private static final String EDITED_USERNAME = "Edited Username";
    private static final String EDITED_PASSWORD = "Edited password";

    private static final String TEAM_TITLE = "Team 1";

    private User loggedUser;

    private User testUser;

    private Team team;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        this.loggedUser = createLoggedUser();
        when(authenticationService.getLoggedUser())
                .thenReturn(loggedUser);

        this.testUser = new User();
        this.team = new Team();
        testUser.setId(USER_ID);
    }

    @Test
    @DisplayName("Should return array of all Users in json format")
    void getAllUsers() {
        UserResponseDTO userResponseDTO = createUserResponseDTO();

        testUser.setUserResponseDtoParameters(userResponseDTO, loggedUser, testUser);

        List<User> users = Collections.singletonList(testUser);

        when(userRepository.findAll())
                .thenReturn(users);

        List<UserResponseDTO> list = users
                .stream().map(User::getResponseDTO)
                .sorted(Comparator.comparing(UserResponseDTO::getId))
                .collect(Collectors.toList());

        List<UserResponseDTO> actualUserList = userService.getAll();

        assertAll(
                () -> assertNotNull(actualUserList),
                () -> assertEquals(list, actualUserList)
        );

        verify(userRepository)
                .findAll();
    }

    @Test
    @DisplayName("Should create User successfully")
    void createUser() {
        UserRequestDTO userRequestDTO = createUserRequestDTO();

        testUser.setUserRequestDtoParameters(userRequestDTO, loggedUser, testUser);

        when(userRepository.save(any()))
                .thenReturn(testUser);

        User actualUser = this.userService.createUser(userRequestDTO, loggedUser);

        assertNotNull(actualUser);

        verify(userRepository).save(any());
    }

    @Test
    @DisplayName("Should edit User successfully")
    void editUser() {
        UserResponseDTO userResponseDTO = createUserResponseDTO();

        testUser.setUserResponseDtoParameters(userResponseDTO, loggedUser, testUser);

        when(userRepository.findById(USER_ID))
                .thenReturn(Optional.of(testUser));

        when(userRepository.save(any(User.class)))
                .thenReturn(testUser);

        User actualUser = userService.updateUser(USER_ID, userResponseDTO, loggedUser);

        assertAll(
                () -> assertNotNull(actualUser),
                () -> assertEquals(testUser, actualUser)
        );

        verify(userRepository)
                .save(any(User.class));
        verify(userRepository)
                .findById(any(Long.class));
    }

    @Test
    @DisplayName("Should delete User by given id")
    void deleteUser() {
        when(userRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(testUser));

        this.userService.deleteById(USER_ID);

        verify(userRepository)
                .deleteById(any(Long.class));
    }

    @Test
    @DisplayName("Should get User by specified id")
    void getUserById() {
        UserResponseDTO userResponseDTO = createUserResponseDTO();

        testUser.setUserResponseDtoParameters(userResponseDTO, loggedUser, testUser);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(testUser));

        UserResponseDTO actualUser = this.userService.getById(USER_ID);

        assertNotNull(actualUser);

        verify(userRepository)
                .findById(anyLong());
    }

    private UserRequestDTO createUserRequestDTO() {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUsername(USER_USERNAME);
        userRequestDTO.setPassword(USER_PASSWORD);
        userRequestDTO.setFirstName(USER_FIRST_NAME);
        userRequestDTO.setLastName(USER_LAST_NAME);

        return userRequestDTO;
    }

    private UserResponseDTO createUserResponseDTO() {
        team.setTitle(TEAM_TITLE);
        testUser.setTeams(Set.of(team));
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername(EDITED_USERNAME);
        userResponseDTO.setPassword(EDITED_PASSWORD);
        userResponseDTO.setFirstName(USER_FIRST_NAME);
        userResponseDTO.setLastName(USER_LAST_NAME);

        return userResponseDTO;
    }

    private User createLoggedUser() {
        User loggedUser = new User();
        loggedUser.setId(10);
        loggedUser.setUsername("admin");
        loggedUser.setPassword("adminpass");
        loggedUser.setAdmin(true);

        return loggedUser;
    }
}