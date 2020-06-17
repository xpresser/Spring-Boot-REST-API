package com.metodi.projectapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.metodi.projectapp.controllers.dtos.TeamRequestDTO;
import com.metodi.projectapp.controllers.dtos.TeamResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "teams")
@JsonPropertyOrder({"id", "title"})
@Getter @Setter @NoArgsConstructor
public class Team extends BaseData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                           CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "team_user", joinColumns = @JoinColumn(name = "team_id"),
                            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnoreProperties("teams")
    private Set<User> users;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                           CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "team_project", joinColumns = @JoinColumn(name = "team_id"),
                               inverseJoinColumns = @JoinColumn(name = "project_id"))
    @JsonIgnoreProperties("teams")
    private Set<Project> projects;

    public void assignUserToTeam(User user) {
        if (this.users == null) {
            this.users = new HashSet<>();
        }

        this.users.add(user);
    }

    public void setTeamRequestDtoParameters(TeamRequestDTO teamRequestDTO,
                                            User loggedUser, Team team) {
        team.setTitle(teamRequestDTO.getTitle());
        team.setCreatorID(loggedUser.getId());
        team.setEditorID(loggedUser.getId());
    }

    public void setTeamResponseDtoParameters(TeamResponseDTO teamResponseDTO,
                                             User loggedUser, Team team) {
        team.setTitle(teamResponseDTO.getTitle());
        team.setEditorID(loggedUser.getId());
    }

    public static TeamResponseDTO getResponseDTO(Team team) {
        TeamResponseDTO teamResponseDTO = new TeamResponseDTO();

        teamResponseDTO.setId(team.getId());
        teamResponseDTO.setTitle(team.getTitle());
        teamResponseDTO.setUsers(team.getUsers().stream().mapToLong(User::getId).boxed().collect(Collectors.toSet()));
        teamResponseDTO.setProjects(team.getProjects().stream().mapToLong(Project::getId).boxed().collect(Collectors.toSet()));
        teamResponseDTO.setDateCreated(team.getDateCreated());
        teamResponseDTO.setDateEdited(team.getDateEdited());
        teamResponseDTO.setCreatorID(team.getCreatorID());
        teamResponseDTO.setEditorID(team.getEditorID());

        return teamResponseDTO;
    }
}
