package com.metodi.projectapp.entities;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.metodi.projectapp.controllers.dtos.TaskRequestDTO;
import com.metodi.projectapp.controllers.dtos.TaskResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tasks")
@JsonPropertyOrder({"id", "title", "description", "status", "projectID, assigneeID"})
@Getter @Setter @NoArgsConstructor
public class Task extends BaseData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    private boolean status;

    @Column(name = "project_id")
    private long projectID;

    @Column(name = "assignee_id")
    private long assigneeID;

    public void setTaskRequestDtoParameters(long projectID, TaskRequestDTO taskRequestDTO,
                                            User loggedUser, Task task) {
        task.setTitle(taskRequestDTO.getTitle());
        task.setDescription(taskRequestDTO.getDescription());
        task.setStatus(taskRequestDTO.isStatus());
        task.setProjectID(projectID);
        task.setAssigneeID(loggedUser.getId());
        task.setCreatorID(loggedUser.getId());
        task.setEditorID(loggedUser.getId());
    }

    public void setTaskResponseDtoParameters(long projectID, TaskResponseDTO taskResponseDTO,
                                             User loggedUser, Task task) {
        task.setTitle(taskResponseDTO.getTitle());
        task.setDescription(taskResponseDTO.getDescription());
        task.setStatus(taskResponseDTO.isStatus());
        task.setProjectID(projectID);
        task.setAssigneeID(taskResponseDTO.getAssigneeID());
        task.setEditorID(loggedUser.getId());
    }
}
