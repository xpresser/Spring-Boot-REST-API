package com.metodi.projectapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.metodi.projectapp.controllers.dtos.UserRequestDTO;
import com.metodi.projectapp.controllers.dtos.UserResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@JsonPropertyOrder({"id", "username", "password", "firstName", "lastName", "isAdmin"})
@Getter @Setter @NoArgsConstructor
public class User extends BaseData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                           CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "team_user", joinColumns = @JoinColumn(name = "user_id"),
                            inverseJoinColumns = @JoinColumn(name = "team_id"))
    @JsonIgnoreProperties("users")
    private Set<Team> teams;

    public void setUserRequestDtoParameters(UserRequestDTO userRequestDTO,
                                            User loggedUser, User user) {
        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(userRequestDTO.getPassword());
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setAdmin(userRequestDTO.isAdmin());
        user.setCreatorID(loggedUser.getId());
        user.setEditorID(loggedUser.getId());
        user.setTeams(userRequestDTO.getTeams());
    }

    public void setUserResponseDtoParameters(UserResponseDTO userResponseDTO,
                                             User loggedUser, User user) {
        user.setUsername(userResponseDTO.getUsername());
        user.setPassword(userResponseDTO.getPassword());
        user.setFirstName(userResponseDTO.getFirstName());
        user.setLastName(userResponseDTO.getLastName());
        user.setAdmin(userResponseDTO.isAdmin());
        user.setEditorID(loggedUser.getId());
    }

    public static UserResponseDTO getResponseDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setId(user.getId());
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setFirstName(user.getFirstName());
        userResponseDTO.setLastName(user.getLastName());
        userResponseDTO.setAdmin(user.isAdmin);
        userResponseDTO.setDateCreated(user.getDateCreated());
        userResponseDTO.setDateEdited(user.getDateEdited());
        userResponseDTO.setCreatorID(user.getCreatorID());
        userResponseDTO.setEditorID(user.getEditorID());
        userResponseDTO.setTeams(user.getTeams().stream().map(Team::getTitle).collect(Collectors.toSet()));

        return userResponseDTO;
    }
}
