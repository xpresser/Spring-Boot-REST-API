package com.metodi.projectapp.services.impl;

import com.metodi.projectapp.controllers.dtos.TaskRequestDTO;
import com.metodi.projectapp.controllers.dtos.TaskResponseDTO;
import com.metodi.projectapp.entities.Task;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.repositories.ProjectRepository;
import com.metodi.projectapp.repositories.TaskRepository;
import com.metodi.projectapp.services.TaskService;
import com.metodi.projectapp.services.exceptions.NotFoundProjectByIdException;
import com.metodi.projectapp.services.exceptions.NotFoundTaskByIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final ProjectRepository projectRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public List<Task> getAll(long projectID, User loggedUser) {
        checkIfProjectIdExists(projectID);

        return this.taskRepository.findAll().stream()
                .filter(task -> task.getProjectID() == projectID)
                .filter(task -> task.getCreatorID() == loggedUser.getId() ||
                        this.projectRepository.getOne(projectID).getTeams()
                                .stream().anyMatch(team -> team.getUsers()
                                .stream().anyMatch(user -> user.getId() == loggedUser.getId())))
                .sorted(Comparator.comparing(Task::getId)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Task createTask(long projectID, TaskRequestDTO taskRequestDTO, User loggedUser) {
        checkIfProjectIdExists(projectID);

        Task task = new Task();
        task.setTaskRequestDtoParameters(projectID, taskRequestDTO, loggedUser, task);

        return this.taskRepository.save(task);
    }

    @Override
    @Transactional
    public Task updateTask(long projectID, long taskID, TaskResponseDTO taskResponseDTO, User loggedUser) {
        checkIfProjectIdExists(projectID);

        Task task = taskRepository.findById(taskID)
                .orElseThrow(() -> new NotFoundTaskByIdException("Task not found with id = " + taskID));

        task.setTaskResponseDtoParameters(projectID, taskResponseDTO, loggedUser, task);

        return this.taskRepository.save(task);
    }

    @Override
    public void deleteById(long projectID, long taskID) {
        checkIfProjectIdExists(projectID);

        checkIfTaskIdExists(taskID);

        this.taskRepository.deleteById(taskID);
    }

    @Override
    public Task getById(long projectID, long taskID) {
        return this.taskRepository.findById(taskID)
                .orElseThrow(() -> new NotFoundTaskByIdException("Task not found with id = " + taskID));
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
