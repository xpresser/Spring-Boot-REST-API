package com.metodi.projectapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.metodi.projectapp.controllers.dtos.ProjectRequestDTO;
import com.metodi.projectapp.controllers.dtos.ProjectResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "projects")
@JsonPropertyOrder({"id", "title", "description"})
@Getter @Setter @NoArgsConstructor
public class Project extends BaseData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "project_id")
    private Set<Task> tasks;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                           CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "team_project", joinColumns = @JoinColumn(name = "project_id"),
                               inverseJoinColumns = @JoinColumn(name = "team_id"))
    @JsonIgnoreProperties("projects")
    private Set<Team> teams;

    public void assignTeamToProject(Team team) {
        if (this.teams == null) {
            this.teams = new HashSet<>();
        }

        this.teams.add(team);
    }

    public void setProjectRequestDtoParameters(ProjectRequestDTO projectRequestDTO,
                                               User loggedUser, Project project) {
        project.setTitle(projectRequestDTO.getTitle());
        project.setDescription(projectRequestDTO.getDescription());
        project.setCreatorID(loggedUser.getId());
        project.setEditorID(loggedUser.getId());
    }

    public void setProjectResponseDtoParameters(ProjectResponseDTO projectResponseDTO,
                                                User loggedUser, Project project) {
        project.setTitle(projectResponseDTO.getTitle());
        project.setDescription(projectResponseDTO.getDescription());
        project.setEditorID(loggedUser.getId());
    }

    public static ProjectResponseDTO getResponseDTO(Project project) {
        ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO();

        projectResponseDTO.setId(project.getId());
        projectResponseDTO.setTitle(project.getTitle());
        projectResponseDTO.setDescription(project.getDescription());
        projectResponseDTO.setDateCreated(project.getDateCreated());
        projectResponseDTO.setDateEdited(project.getDateEdited());
        projectResponseDTO.setCreatorID(project.getCreatorID());
        projectResponseDTO.setEditorID(project.getEditorID());
        projectResponseDTO.setTasks(project.getTasks().stream().mapToLong(Task::getId).boxed().collect(Collectors.toSet()));
        projectResponseDTO.setTeams(project.getTeams().stream().map(Team::getTitle).collect(Collectors.toSet()));

        return projectResponseDTO;
    }
}
