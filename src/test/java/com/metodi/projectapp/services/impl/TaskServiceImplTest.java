package com.metodi.projectapp.services.impl;

import com.metodi.projectapp.controllers.dtos.TaskRequestDTO;
import com.metodi.projectapp.controllers.dtos.TaskResponseDTO;
import com.metodi.projectapp.entities.Project;
import com.metodi.projectapp.entities.Task;
import com.metodi.projectapp.entities.Team;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.repositories.ProjectRepository;
import com.metodi.projectapp.repositories.TaskRepository;
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
class TaskServiceImplTest {

    private static final long LOGGED_USER_ID = 10;

    private static final long TASK_ID = 3;
    private static final long PROJECT_ID = 2;

    private static final String TASK_TITLE = "task title";
    private static final String TASK_DESCRIPTION = "task description";

    private static final String EDITED_TASK_TITLE = "edited task title";
    private static final String EDITED_TASK_DESCRIPTION = "edited task description";

    private User loggedUser;

    private Task testTask;

    private Project project;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private ProjectRepository projectRepository;

    @Autowired
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        createLoggedUser();

        when(authenticationService.getLoggedUser())
                .thenReturn(loggedUser);

        this.testTask = createTestTask();
        this.project = createTestProject();
    }

    @Test
    @DisplayName("Should return json array of all tasks from selected project id")
    void getAll() {
        List<Task> tasks = Collections.singletonList(testTask);

        when(projectRepository.findById(PROJECT_ID))
                .thenReturn(java.util.Optional.of(project));

        when(taskRepository.findAll())
                .thenReturn(tasks);

        when(projectRepository.getOne(PROJECT_ID))
                .thenReturn(project);

        List<Task> list = tasks
                .stream()
                .filter(task -> task.getProjectID() == PROJECT_ID)
                .filter(task -> task.getCreatorID() == loggedUser.getId() ||
                        this.projectRepository.getOne(PROJECT_ID).getTeams()
                                .stream().anyMatch(team -> team.getUsers()
                                .stream().anyMatch(user -> user.getId() == loggedUser.getId())))
                .sorted(Comparator.comparing(Task::getId)).collect(Collectors.toList());

        List<Task> actualTaskList = taskService.getAll(PROJECT_ID, loggedUser);

        assertAll(
                () -> assertNotNull(actualTaskList),
                () -> assertEquals(list, actualTaskList)
        );

        verify(taskRepository)
                .findAll();
    }

    @Test
    @DisplayName("Should create Task successfully in chosen project by id")
    void createTask() {
        TaskRequestDTO taskRequestDTO = createTaskRequestDTO();

        testTask.setTaskRequestDtoParameters(PROJECT_ID, taskRequestDTO, loggedUser, testTask);

        when(projectRepository.findById(PROJECT_ID))
                .thenReturn(java.util.Optional.ofNullable(project));

        when(taskRepository.save(any()))
                .thenReturn(testTask);

        Task actualTask = this.taskService.createTask(PROJECT_ID, taskRequestDTO, loggedUser);

        assertNotNull(actualTask);

        verify(taskRepository).save(any());
    }

    @Test
    @DisplayName("Should edit Task successfully from selected project by id")
    void editTask() {
        TaskResponseDTO taskResponseDTO = createTaskResponseDTO();

        when(projectRepository.findById(PROJECT_ID))
                .thenReturn(java.util.Optional.ofNullable(project));

        when(taskRepository.findById(TASK_ID))
                .thenReturn(java.util.Optional.ofNullable(testTask));

        testTask.setTaskResponseDtoParameters(PROJECT_ID, taskResponseDTO, loggedUser, testTask);

        when(taskRepository.save(any(Task.class)))
                .thenReturn(testTask);

        Task actualTask = taskService.updateTask(PROJECT_ID, TASK_ID, taskResponseDTO, loggedUser);

        assertAll(
                () -> assertNotNull(actualTask),
                () -> assertEquals(testTask, actualTask)
        );

        verify(taskRepository)
                .save(any(Task.class));

        verify(taskRepository)
                .findById(anyLong());
    }

    @Test
    @DisplayName("Should delete Task by given id from selected project by id")
    void deleteTask() {
        when(projectRepository.findById(PROJECT_ID))
                .thenReturn(java.util.Optional.ofNullable(project));

        when(taskRepository.findById(TASK_ID))
                .thenReturn(java.util.Optional.ofNullable(testTask));

        this.taskService.deleteById(PROJECT_ID, TASK_ID);

        verify(taskRepository)
                .deleteById(anyLong());
    }

    @Test
    @DisplayName("Should get Task by specified id from selected project by id")
    void getTaskById() {
        when(taskRepository.findById(TASK_ID))
                .thenReturn(java.util.Optional.ofNullable(testTask));

        Task actualTask = this.taskService.getById(PROJECT_ID, TASK_ID);

        assertAll(
                () -> assertNotNull(actualTask),
                () -> assertEquals(testTask, actualTask)
        );

        verify(taskRepository)
                .findById(anyLong());
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

    private Task createTestTask() {
        Task task = new Task();
        task.setId(TASK_ID);
        task.setTitle(TASK_TITLE);
        task.setDescription(TASK_DESCRIPTION);
        task.setProjectID(PROJECT_ID);

        return task;
    }

    private Project createTestProject() {
        Team team = createTestTeam();
        Project project = new Project();
        project.setId(PROJECT_ID);
        project.setTeams(Set.of(team));

        return project;
    }

    private Team createTestTeam() {
        Team team = new Team();
        team.setUsers(Set.of(loggedUser));

        return team;
    }

    private void createLoggedUser() {
        loggedUser = new User();
        loggedUser.setId(LOGGED_USER_ID);
        loggedUser.setAdmin(true);
    }
}