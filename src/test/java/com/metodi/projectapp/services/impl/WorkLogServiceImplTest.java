package com.metodi.projectapp.services.impl;

import com.metodi.projectapp.controllers.dtos.WorkLogRequestDTO;
import com.metodi.projectapp.controllers.dtos.WorkLogResponseDTO;
import com.metodi.projectapp.entities.Project;
import com.metodi.projectapp.entities.Task;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.entities.WorkLog;
import com.metodi.projectapp.repositories.ProjectRepository;
import com.metodi.projectapp.repositories.TaskRepository;
import com.metodi.projectapp.repositories.WorkLogRepository;
import com.metodi.projectapp.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Date;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class WorkLogServiceImplTest {

    private static final long LOGGED_USER_ID = 10;

    private static final long WORKLOG_ID = 4;
    private static final long TASK_ID = 3;
    private static final long USER_ID = 13;
    private static final int HOURS_SPENT = 8;
    private static final String DATE_WORKED = "2020-05-02";

    private static final long PROJECT_ID = 2;

    private static final int EDITED_HOURS_SPENT = 4;

    private User loggedUser;

    private WorkLog testWorkLog;

    private Project project;

    private Task task;

    @MockBean
    private WorkLogRepository workLogRepository;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private WorkLogServiceImpl workLogService;

    @BeforeEach
    void setUp() {
        createLoggedUser();

        when(authenticationService.getLoggedUser())
                .thenReturn(loggedUser);
        this.testWorkLog = createTestWorkLog();
        this.project = createTestProject();
        this.task = createTestTask();
    }

    @Test
    @DisplayName("Should return json array of all worklogs from selected task in a project by id")
    void getAll() {
        List<WorkLog> workLogs = Collections.singletonList(testWorkLog);

        when(projectRepository.findById(PROJECT_ID))
                .thenReturn(java.util.Optional.ofNullable(project));

        when(taskRepository.findById(TASK_ID))
                .thenReturn(java.util.Optional.ofNullable(task));

        when(workLogRepository.findAll())
                .thenReturn(workLogs);

        List<WorkLog> list = workLogs
                .stream()
                .filter(workLog -> workLog.getTaskID() == TASK_ID)
                .sorted(Comparator.comparing(WorkLog::getId)).collect(Collectors.toList());

        List<WorkLog> actualWorkLogList = workLogService.getAll(PROJECT_ID, TASK_ID);

        assertAll(
                () -> assertNotNull(actualWorkLogList),
                () -> assertEquals(list, actualWorkLogList)
        );

        verify(workLogRepository)
                .findAll();
    }

    @Test
    @DisplayName("Should create WorkLog successfully for a given task from a selected project by id")
    void createTask() {
        WorkLogRequestDTO workLogRequestDTO = createWorkLogRequestDTO();

        when(projectRepository.findById(PROJECT_ID))
                .thenReturn(java.util.Optional.ofNullable(project));

        when(taskRepository.findById(TASK_ID))
                .thenReturn(java.util.Optional.ofNullable(task));

        testWorkLog.setWorkLogRequestDtoParameters(TASK_ID, loggedUser, workLogRequestDTO, testWorkLog);

        when(workLogRepository.save(any()))
                .thenReturn(testWorkLog);

        WorkLog actualWorkLog = this.workLogService.createWorkLog(PROJECT_ID, TASK_ID, workLogRequestDTO, loggedUser);

        assertNotNull(actualWorkLog);

        verify(workLogRepository).save(any());
    }

    @Test
    @DisplayName("Should edit WorkLog successfully for a given task from a selected project by id")
    void editWorkLog() {
        WorkLogResponseDTO workLogResponseDTO = createWorkLogResponseDTO();

        when(projectRepository.findById(PROJECT_ID))
                .thenReturn(java.util.Optional.ofNullable(project));

        when(taskRepository.findById(TASK_ID))
                .thenReturn(java.util.Optional.ofNullable(task));


        when(workLogRepository.findById(WORKLOG_ID))
                .thenReturn(java.util.Optional.ofNullable(testWorkLog));

        testWorkLog.setWorkLogResponseDtoParameters(TASK_ID, loggedUser, workLogResponseDTO, testWorkLog);

        when(workLogRepository.save(any()))
                .thenReturn(testWorkLog);

        WorkLog actualWorkLog = this.workLogService.updateWorkLog(PROJECT_ID, TASK_ID, WORKLOG_ID, workLogResponseDTO, loggedUser);

        assertAll(
                () -> assertNotNull(actualWorkLog),
                () -> assertEquals(testWorkLog, actualWorkLog)
        );

        verify(workLogRepository)
                .save(any());

        verify(workLogRepository)
                .findById(anyLong());
    }

    @Test
    @DisplayName("Should delete WorkLog by given id for a given task from selected project")
    void deleteWorkLog() {
        when(projectRepository.findById(PROJECT_ID))
                .thenReturn(java.util.Optional.ofNullable(project));

        when(taskRepository.findById(TASK_ID))
                .thenReturn(java.util.Optional.ofNullable(task));

        when(workLogRepository.findById(WORKLOG_ID))
                .thenReturn(java.util.Optional.ofNullable(testWorkLog));

        this.workLogService.deleteById(PROJECT_ID, TASK_ID, WORKLOG_ID);

        verify(workLogRepository)
                .deleteById(anyLong());
    }

    @Test
    @DisplayName("Should get WorkLog by specified id from selected task and project")
    void getWorkLogById() {
        when(projectRepository.findById(PROJECT_ID))
                .thenReturn(java.util.Optional.ofNullable(project));

        when(taskRepository.findById(TASK_ID))
                .thenReturn(java.util.Optional.ofNullable(task));

        when(workLogRepository.findById(WORKLOG_ID))
                .thenReturn(java.util.Optional.ofNullable(testWorkLog));

        WorkLog actualWorkLog = this.workLogService.getById(PROJECT_ID, TASK_ID, WORKLOG_ID);

        assertAll(
                () -> assertNotNull(actualWorkLog),
                () -> assertEquals(testWorkLog, actualWorkLog)
        );

        verify(workLogRepository)
                .findById(anyLong());
    }

    private WorkLogRequestDTO createWorkLogRequestDTO() {
        WorkLogRequestDTO workLogRequestDTO = new WorkLogRequestDTO();
        workLogRequestDTO.setTaskID(TASK_ID);
        workLogRequestDTO.setUserID(USER_ID);
        workLogRequestDTO.setHoursSpent(HOURS_SPENT);
        workLogRequestDTO.setDateWorked(Date.valueOf(DATE_WORKED));

        return workLogRequestDTO;
    }

    private WorkLogResponseDTO createWorkLogResponseDTO() {
        WorkLogResponseDTO workLogResponseDTO = new WorkLogResponseDTO();
        workLogResponseDTO.setTaskID(TASK_ID);
        workLogResponseDTO.setUserID(USER_ID);
        workLogResponseDTO.setHoursSpent(EDITED_HOURS_SPENT);
        workLogResponseDTO.setDateWorked(Date.valueOf(DATE_WORKED));

        return workLogResponseDTO;
    }

    private WorkLog createTestWorkLog() {
        WorkLog workLog = new WorkLog();
        workLog.setId(WORKLOG_ID);
        workLog.setTaskID(TASK_ID);
        workLog.setUserID(USER_ID);
        workLog.setHoursSpent(HOURS_SPENT);
        workLog.setDateWorked(Date.valueOf(DATE_WORKED));

        return workLog;
    }

    private Project createTestProject() {
        Project project = new Project();
        project.setId(PROJECT_ID);

        return project;
    }

    private Task createTestTask() {
        Task task = new Task();
        task.setId(TASK_ID);

        return task;
    }

    private void createLoggedUser() {
        loggedUser = new User();
        loggedUser.setId(LOGGED_USER_ID);
        loggedUser.setAdmin(true);
    }
}