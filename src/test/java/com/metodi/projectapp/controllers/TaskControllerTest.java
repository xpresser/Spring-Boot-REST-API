package com.metodi.projectapp.controllers;

import com.metodi.projectapp.controllers.dtos.TaskRequestDTO;
import com.metodi.projectapp.controllers.dtos.TaskResponseDTO;
import com.metodi.projectapp.entities.Task;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.services.AuthenticationService;
import com.metodi.projectapp.services.impl.JWTTokenService;
import com.metodi.projectapp.services.TaskService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    private static final String LOGGED_USER = "admin,adminpass";
    private static final int PROJECT_ID = 13;

    private static final int TASK_ID = 1;
    private static final String TASK_TITLE = "Create Tests";
    private static final String TASK_DESCRIPTION = "My first task";

    private static final String EDITED_TASK_TITLE = "Edited task title";
    private static final String EDITED_TASK_DESCRIPTION = "Edited task description";

    private static final String CREATE_TASK_JSON = "{\"id\":0,\"title\":\"Create Tests\",\"description\":\"My first task\"," +
            "\"status\":false,\"dateCreated\":null,\"dateEdited\":null,\"creatorID\":0,\"editorID\":0,\"projectID\":0,\"assigneeID\":0}";

    private static final String EDIT_TASK_JSON = "{\"id\":0,\"title\":\"Edited task title\",\"description\":\"Edited task description\"," +
            "\"status\":false,\"dateCreated\":null,\"dateEdited\":null,\"creatorID\":0,\"editorID\":0,\"projectID\":0,\"assigneeID\":0}";

    private static final String GET_TASK_BY_ID_JSON = "{\"id\":0,\"title\":\"Edited task title\",\"description\":\"Edited task description\"," +
            "\"status\":false,\"dateCreated\":null,\"dateEdited\":null,\"creatorID\":0,\"editorID\":0,\"projectID\":0,\"assigneeID\":0}";

    private User loggedUser;

    private Task testTask;

    @MockBean
    private TaskService taskService;

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
    @DisplayName("getAll() should return json array of all existing tasks from chosen project by id")
    void getAll() throws Exception {
        testTask = createTestTask();
        List<Task> tasks = Collections.singletonList(testTask);

        when(taskService.getAll(PROJECT_ID, loggedUser))
                .thenReturn(tasks);

        mockMvc.perform(get("/projects/{projectID}/tasks/", PROJECT_ID)
                        .header("Authorization", LOGGED_USER)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(testTask.getTitle())))
                .andExpect(jsonPath("$[0].description", is(testTask.getDescription())));

        verify(taskService)
                .getAll(anyLong(), any());
    }

    @Test
    @DisplayName("createTask() should return the task that was just created in selected project")
    void createTask() throws Exception {
        TaskRequestDTO taskRequestDTO = createTaskRequestDTO();

        setTestTaskParameters(taskRequestDTO);

        when(taskService.createTask(anyLong(), any(), any()))
                .thenReturn(testTask);

        mockMvc.perform(post("/projects/{projectID}/tasks", PROJECT_ID)
                        .header("Authorization", LOGGED_USER)
                        .content(CREATE_TASK_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(CREATE_TASK_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(taskService)
                .createTask(anyLong(), any(), any());
    }

    @Test
    @DisplayName("editTask() should return updated task in json format")
    void editTask() throws Exception {
        TaskResponseDTO taskResponseDTO = createTaskResponseDTO();

        setTestTaskParameters(taskResponseDTO);

        when(taskService.updateTask(PROJECT_ID, TASK_ID, taskResponseDTO, loggedUser))
                .thenReturn(testTask);

        mockMvc.perform(put("/projects/{projectID}/tasks/{taskID}", PROJECT_ID, TASK_ID)
                        .header("Authorization", LOGGED_USER)
                        .content(EDIT_TASK_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(taskService)
                .updateTask(anyLong(), anyLong(), any(), any());
    }

    @Test
    @DisplayName("getById() should return task in json format from selected project by id")
    void getById() throws Exception {
        TaskResponseDTO taskResponseDTO = createTaskResponseDTO();

        setTestTaskParameters(taskResponseDTO);

        when(taskService.getById(PROJECT_ID, TASK_ID))
                .thenReturn(testTask);

        mockMvc.perform(get("/projects/{projectID}/tasks/{taskID}", PROJECT_ID, TASK_ID))
                        .andExpect(status().isOk())
                        .andExpect(content().json(GET_TASK_BY_ID_JSON));

        verify(taskService)
                .getById(PROJECT_ID, TASK_ID);
    }

    @Test
    @DisplayName("deleteById() should delete task by id from selected project by id")
    void deleteById() throws Exception {
        mockMvc.perform(delete("/projects/{projectID}/tasks/{taskID}", PROJECT_ID, TASK_ID)
                        .header("Authorization", LOGGED_USER))
                        .andExpect(status().isOk());

        verify(taskService)
                .deleteById(PROJECT_ID, TASK_ID);
    }

    private void setTestTaskParameters(TaskRequestDTO taskRequestDTO) {
        testTask = new Task();
        testTask.setTitle(taskRequestDTO.getTitle());
        testTask.setDescription(taskRequestDTO.getDescription());
    }

    private void setTestTaskParameters(TaskResponseDTO taskResponseDTO) {
        testTask = new Task();
        testTask.setTitle(taskResponseDTO.getTitle());
        testTask.setDescription(taskResponseDTO.getDescription());
    }

    private Task createTestTask() {
        Task task = new Task();
        task.setTitle(TASK_TITLE);
        task.setDescription(TASK_DESCRIPTION);

        return task;
    }

    private TaskRequestDTO createTaskRequestDTO() {
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO();
        taskRequestDTO.setTitle(TASK_TITLE);
        taskRequestDTO.setDescription(TASK_DESCRIPTION);

        return taskRequestDTO;
    }

    private TaskResponseDTO createTaskResponseDTO() {
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setTitle(EDITED_TASK_TITLE);
        taskResponseDTO.setDescription(EDITED_TASK_DESCRIPTION);

        return taskResponseDTO;
    }
}