package com.metodi.projectapp.controllers;

import com.metodi.projectapp.controllers.dtos.WorkLogRequestDTO;
import com.metodi.projectapp.controllers.dtos.WorkLogResponseDTO;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.entities.WorkLog;
import com.metodi.projectapp.services.AuthenticationService;
import com.metodi.projectapp.services.impl.JWTTokenService;
import com.metodi.projectapp.services.WorkLogService;
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

import java.sql.Date;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkLogController.class)
class WorkLogControllerTest {

    private static final String LOGGED_USER = "admin,adminpass";
    private static final long PROJECT_ID = 5;

    private static final int WORKLOG_ID = 1;
    private static final long USER_ID = 13;
    private static final long TASK_ID = 8;
    private static final int HOURS_SPENT = 4;
    private static final String DATE_WORKED = "2020-01-05";

    private static final int EDITED_HOURS_SPENT = 3;

    private static final String CREATE_WORKLOG_JSON = "{\"id\":1,\"taskID\":8,\"userID\":13,\"hoursSpent\":4," +
            "\"dateWorked\":\"2020-01-05\"}";

    private static final String EDITED_WORKLOG_JSON = "{\"id\":1,\"taskID\":8,\"userID\":13,\"hoursSpent\":3," +
            "\"dateWorked\":\"2020-01-05\"}";

    private static final String GET_BY_ID_WORKLOG_JSON = "{\"id\":1,\"taskID\":8,\"userID\":13,\"hoursSpent\":3," +
            "\"dateWorked\":\"2020-01-05\"}";

    private static final String GET_ALL_CREATE_PATH = "/projects/{projectID}/tasks/{taskID}/worklogs";
    private static final String UPDATE_GET_DELETE_PATH = "/projects/{projectID}/tasks/{taskID}/worklogs/{worklogID}";

    private User loggedUser;

    private WorkLog testWorkLog;

    @MockBean
    private WorkLogService workLogService;

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
    @DisplayName("getAll() should return json array of all existing worklogs")
    void getAll() throws Exception {
        WorkLog workLog = createTestWorkLog();
        List<WorkLog> worklogs = Collections.singletonList(workLog);

        when(workLogService.getAll(PROJECT_ID, TASK_ID))
                .thenReturn(worklogs);

        mockMvc.perform(get(GET_ALL_CREATE_PATH, PROJECT_ID, TASK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(workLogService)
                .getAll(anyLong(), anyLong());
    }

    @Test
    @DisplayName("createWorkLog() should return the worklog that was just created")
    void createWorkLog() throws Exception {
        WorkLogRequestDTO workLogRequestDTO = createWorkLogRequestDTO();

        setTestWorkLogParameters(workLogRequestDTO);

        when(workLogService.createWorkLog(anyLong(), anyLong(), any(), any()))
                .thenReturn(testWorkLog);

        mockMvc.perform(post(GET_ALL_CREATE_PATH, PROJECT_ID, TASK_ID)
                            .header("Authorization", LOGGED_USER)
                            .content(CREATE_WORKLOG_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("utf-8")
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(CREATE_WORKLOG_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(workLogService)
                .createWorkLog(anyLong(), anyLong(), any(), any());
    }

    @Test
    @DisplayName("editWorkLog() should return updated worklog in json format")
    void editWorkLog() throws Exception {
        WorkLogResponseDTO workLogResponseDTO = createWorkLogResponseDTO();

        setTestWorkLogParameters(workLogResponseDTO);

        when(workLogService.updateWorkLog(anyLong(), anyLong(), anyLong(), any(), any()))
                .thenReturn(testWorkLog);

        mockMvc.perform(put(UPDATE_GET_DELETE_PATH, PROJECT_ID, TASK_ID, WORKLOG_ID)
                        .header("Authorization", LOGGED_USER)
                        .content(EDITED_WORKLOG_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(EDITED_WORKLOG_JSON))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(workLogService)
                .updateWorkLog(anyLong(), anyLong(), anyLong(), any(), any());
    }

    @Test
    @DisplayName("getById() should return worklog in json format from selected task and project by id")
    void getById() throws Exception {
        WorkLogResponseDTO workLogResponseDTO = createWorkLogResponseDTO();

        setTestWorkLogParameters(workLogResponseDTO);

        when(workLogService.getById(PROJECT_ID, TASK_ID, WORKLOG_ID))
                .thenReturn(testWorkLog);

        mockMvc.perform(get(UPDATE_GET_DELETE_PATH, PROJECT_ID, TASK_ID, WORKLOG_ID)
                        .header("Authorization", LOGGED_USER))
        .andExpect(status().isOk())
        .andExpect(content().json(GET_BY_ID_WORKLOG_JSON))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(workLogService)
                .getById(PROJECT_ID, TASK_ID, WORKLOG_ID);
    }

    @Test
    @DisplayName("deleteById() should delete worklog by id from selected task and project")
    void deleteById() throws Exception {
        mockMvc.perform(delete(UPDATE_GET_DELETE_PATH, PROJECT_ID, TASK_ID, WORKLOG_ID)
                        .header("Authorization", LOGGED_USER))
                        .andExpect(status().isOk());

        verify(workLogService)
                .deleteById(PROJECT_ID, TASK_ID, WORKLOG_ID);
    }

    private void setTestWorkLogParameters(WorkLogRequestDTO workLogRequestDTO) {
        testWorkLog = new WorkLog();
        testWorkLog.setId(WORKLOG_ID);
        testWorkLog.setTaskID(workLogRequestDTO.getTaskID());
        testWorkLog.setUserID(workLogRequestDTO.getUserID());
        testWorkLog.setHoursSpent(workLogRequestDTO.getHoursSpent());
        testWorkLog.setDateWorked(workLogRequestDTO.getDateWorked());
    }

    private void setTestWorkLogParameters(WorkLogResponseDTO workLogResponseDTO) {
        testWorkLog = new WorkLog();
        testWorkLog.setId(WORKLOG_ID);
        testWorkLog.setTaskID(workLogResponseDTO.getTaskID());
        testWorkLog.setUserID(workLogResponseDTO.getUserID());
        testWorkLog.setHoursSpent(workLogResponseDTO.getHoursSpent());
        testWorkLog.setDateWorked(workLogResponseDTO.getDateWorked());
    }

    private WorkLog createTestWorkLog() {
        WorkLog workLog = new WorkLog();
        workLog.setUserID(USER_ID);
        workLog.setTaskID(TASK_ID);
        workLog.setHoursSpent(HOURS_SPENT);
        workLog.setDateWorked(Date.valueOf(DATE_WORKED));

        return workLog;
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
}