package com.metodi.projectapp.controllers;

import com.metodi.projectapp.controllers.dtos.UserRequestDTO;
import com.metodi.projectapp.controllers.dtos.UserResponseDTO;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.services.AuthenticationService;
import com.metodi.projectapp.services.impl.JWTTokenService;
import com.metodi.projectapp.services.UserService;
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

@WebMvcTest(UserController.class)
class UserControllerTest {

    private static final long USER_ID = 1;
    private static final String USER_USERNAME = "xpresser";
    private static final String USER_PASSWORD = "123";

    private static final String GET_BY_ID_USER_JSON = "{\"id\":1,\"username\":\"xpresser\"," +
            "\"firstName\":null,\"lastName\":null,\"dateCreated\":null,\"dateEdited\":null," +
            "\"creatorID\":0,\"editorID\":0,\"password\":\"123\",\"teams\":null,\"admin\":false}";

    private static final String LOGGED_USER = "admin,adminpass";
    private static final String CREATE_USER_JSON = "{\"username\":\"Test\",\"password\":\"password\"," +
            "\"firstName\":\"t\",\"lastName\":\"t\",\"admin\":false}";

    private static final String EDIT_USER_JSON = "{\"username\":\"edited\",\"password\":\"editedPass\"," +
            "\"firstName\":\"t\",\"lastName\":\"t\",\"admin\":false}";

    private User loggedUser;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    /**
     * Mocking UserService interface instead of implementation
     * due to testing the UserController
     */
    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JWTTokenService tokenService;

    private User testUser;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.loggedUser = new User();
        when(authenticationService.getLoggedUser())
                .thenReturn(loggedUser);
    }

    @Test
    @DisplayName("getAll() should return json list of all existing users")
    void getAll() throws Exception {
        UserResponseDTO userResponseDTO = createUserResponseDTO();
        List<UserResponseDTO> users = Collections.singletonList(userResponseDTO);

        when(userService.getAll())
                .thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(userResponseDTO.getUsername())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("createUser() should return the user that was just created in json format")
    void createUser() throws Exception {
        UserRequestDTO userRequestDTO = createUserRequestDTO();

        setTestUserParameters(userRequestDTO);

        when(userService.createUser(any(), any()))
                .thenReturn(testUser);

        mockMvc.perform(post("/users")
                        .header("Authorization", LOGGED_USER)
                        .content(CREATE_USER_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(CREATE_USER_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(userService)
                .createUser(any(), any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("editUser() should return status 200 after successfully editing a user")
    void editUser() throws Exception {
        UserResponseDTO userResponseDTO = createUserResponseDTO();

        setTestUserParameters(userResponseDTO);

        when(userService.updateUser(USER_ID, userResponseDTO, loggedUser))
                .thenReturn(testUser);

        mockMvc.perform(put("/users/{id}", USER_ID)
                        .header("Authorization", LOGGED_USER)
                        .content(EDIT_USER_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService)
                .updateUser(any(Long.class), any(), any());
    }

    @Test
    @DisplayName("getById() should return user in json format by specified id ")
    void getById() throws Exception {
        UserResponseDTO user = createUserResponseDTO();

        when(userService.getById(USER_ID))
                .thenReturn(user);

        mockMvc.perform(get("/users/{id}", USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(GET_BY_ID_USER_JSON));

        verify(userService)
                .getById(USER_ID);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("deleteById() should delete user by specified id")
    void deleteById() throws Exception {

        mockMvc.perform(delete("/users/{userID}", USER_ID)
                        .header("Authorization", LOGGED_USER))
                        .andExpect(status().isOk());

        verify(userService)
                .deleteById(USER_ID);
    }

    private void setTestUserParameters(UserRequestDTO userRequestDTO) {
        testUser = new User();
        testUser.setUsername(userRequestDTO.getUsername());
        testUser.setPassword(userRequestDTO.getPassword());
        testUser.setFirstName(userRequestDTO.getFirstName());
        testUser.setLastName(userRequestDTO.getLastName());
        testUser.setAdmin(userRequestDTO.isAdmin());
        testUser.setCreatorID(loggedUser.getId());
        testUser.setEditorID(loggedUser.getId());
        testUser.setTeams(userRequestDTO.getTeams());
    }

    private void setTestUserParameters(UserResponseDTO userResponseDTO) {
        testUser = new User();
        testUser.setUsername(userResponseDTO.getUsername());
        testUser.setPassword(userResponseDTO.getPassword());
        testUser.setFirstName(userResponseDTO.getFirstName());
        testUser.setLastName(userResponseDTO.getLastName());
        testUser.setAdmin(userResponseDTO.isAdmin());
        testUser.setCreatorID(loggedUser.getId());
        testUser.setEditorID(loggedUser.getId());
    }

    private UserRequestDTO createUserRequestDTO() {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUsername("Test");
        userRequestDTO.setPassword("password");
        userRequestDTO.setFirstName("t");
        userRequestDTO.setLastName("t");
        userRequestDTO.setAdmin(false);

        return userRequestDTO;
    }

    private UserResponseDTO createUserResponseDTO() {
        UserResponseDTO user = new UserResponseDTO();
        user.setId(USER_ID);
        user.setUsername(USER_USERNAME);
        user.setPassword(USER_PASSWORD);

        return user;
    }
}