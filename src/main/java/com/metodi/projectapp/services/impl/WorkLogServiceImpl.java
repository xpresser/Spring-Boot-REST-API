package com.metodi.projectapp.services.impl;

import com.metodi.projectapp.controllers.dtos.WorkLogRequestDTO;
import com.metodi.projectapp.controllers.dtos.WorkLogResponseDTO;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.entities.WorkLog;
import com.metodi.projectapp.repositories.ProjectRepository;
import com.metodi.projectapp.repositories.TaskRepository;
import com.metodi.projectapp.repositories.WorkLogRepository;
import com.metodi.projectapp.services.WorkLogService;
import com.metodi.projectapp.services.exceptions.NotFoundProjectByIdException;
import com.metodi.projectapp.services.exceptions.NotFoundTaskByIdException;
import com.metodi.projectapp.services.exceptions.NotFoundWorkLogByIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkLogServiceImpl implements WorkLogService {

    private final WorkLogRepository workLogRepository;

    private final ProjectRepository projectRepository;

    private final TaskRepository taskRepository;

    @Autowired
    public WorkLogServiceImpl(WorkLogRepository workLogRepository,
                              ProjectRepository projectRepository,
                              TaskRepository taskRepository) {
        this.workLogRepository = workLogRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public List<WorkLog> getAll(long projectID, long taskID) {
        checkIfProjectIdExists(projectID);

        checkIfTaskIdExists(taskID);

        return workLogRepository.findAll().stream().filter(workLog -> workLog.getTaskID() == taskID)
                .sorted(Comparator.comparing(WorkLog::getId)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WorkLog createWorkLog(long projectID, long taskID,
                                 WorkLogRequestDTO workLogRequestDTO, User loggedUser) {
        checkIfProjectIdExists(projectID);

        checkIfTaskIdExists(taskID);

        WorkLog workLog = new WorkLog();
        workLog.setWorkLogRequestDtoParameters(taskID, loggedUser, workLogRequestDTO, workLog);

        return this.workLogRepository.save(workLog);
    }

    @Override
    @Transactional
    public WorkLog updateWorkLog(long projectID, long taskID, long workLogID,
                                 WorkLogResponseDTO workLogResponseDTO, User loggedUser) {
        checkIfProjectIdExists(projectID);

        checkIfTaskIdExists(taskID);

        WorkLog workLog = this.workLogRepository.findById(workLogID)
                .orElseThrow(() -> new NotFoundWorkLogByIdException("WorkLog not found with id = " + workLogID));

        workLog.setWorkLogResponseDtoParameters(taskID, loggedUser, workLogResponseDTO, workLog);

        return this.workLogRepository.save(workLog);
    }

    @Override
    public void deleteById(long projectID, long taskID, long workLogID) {
        checkIfProjectIdExists(projectID);

        checkIfTaskIdExists(taskID);

        checkIfWorkLogIdExists(workLogID);

        this.workLogRepository.deleteById(workLogID);
    }

    @Override
    public WorkLog getById(long projectID, long taskID, long workLogID) {
        checkIfProjectIdExists(projectID);

        checkIfTaskIdExists(taskID);

        return this.workLogRepository.findById(workLogID)
                .orElseThrow(() -> new NotFoundWorkLogByIdException("WorkLog not found with id = " + workLogID));
    }

    private void checkIfWorkLogIdExists(long workLogID) {
        if (this.workLogRepository.findById(workLogID).isEmpty()) {
            throw new NotFoundWorkLogByIdException("WorkLog not found with id = " + workLogID);
        }
    }

    private void checkIfProjectIdExists(long projectID) {
        if (this.projectRepository.findById(projectID).isEmpty()) {
            throw new NotFoundProjectByIdException("Project not found with id = " + projectID);
        }
    }

    private void checkIfTaskIdExists(long taskID) {
        if (this.taskRepository.findById(taskID).isEmpty()) {
            throw new NotFoundTaskByIdException("Task not found with id = " + taskID);
        }
    }
}
