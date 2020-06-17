package com.metodi.projectapp.services.impl;

import com.metodi.projectapp.controllers.dtos.TeamRequestDTO;
import com.metodi.projectapp.controllers.dtos.TeamResponseDTO;
import com.metodi.projectapp.entities.Team;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.repositories.TeamRepository;
import com.metodi.projectapp.repositories.UserRepository;
import com.metodi.projectapp.services.TeamService;
import com.metodi.projectapp.services.exceptions.NotFoundTeamByIdException;
import com.metodi.projectapp.services.exceptions.NotFoundUserByIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    private final UserRepository userRepository;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository,
                           UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<TeamResponseDTO> getAll() {
        return this.teamRepository.findAll().stream().map(Team::getResponseDTO)
                .sorted(Comparator.comparing(TeamResponseDTO::getId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Team createTeam(TeamRequestDTO teamRequestDTO, User loggedUser) {
        Team team = new Team();
        team.setTeamRequestDtoParameters(teamRequestDTO, loggedUser, team);

        return this.teamRepository.save(team);
    }

    @Override
    @Transactional
    public Team updateTeam(long teamID, TeamResponseDTO teamResponseDTO, User loggedUser) {
        Team team = teamRepository.findById(teamID)
                .orElseThrow(() -> new NotFoundTeamByIdException("Team not found with id = " + teamID));
        
        team.setTeamResponseDtoParameters(teamResponseDTO, loggedUser, team);

        return this.teamRepository.save(team);
    }

    @Override
    public void deleteById(long teamID) {
        checkIfTeamIdExists(teamID);

        this.teamRepository.deleteById(teamID);
    }

    @Override
    public TeamResponseDTO getById(long teamID) {
        return this.teamRepository.findById(teamID).map(Team::getResponseDTO)
                .orElseThrow(() -> new NotFoundTeamByIdException("Team not found with id = " + teamID));
    }

    @Override
    public void assignUserById(long teamID, long userID) {
        checkIfTeamIdExists(teamID);

        checkIfUserIdExists(userID);

        Team team = this.teamRepository.getOne(teamID);
        User user = this.userRepository.getOne(userID);

        team.assignUserToTeam(user);

        this.teamRepository.save(team);
    }

    private void checkIfTeamIdExists(long teamID) {
        if (this.teamRepository.findById(teamID).isEmpty()) {
            throw new NotFoundTeamByIdException("Team not found with id = " + teamID);
        }
    }

    private void checkIfUserIdExists(long userID) {
        if (this.userRepository.findById(userID).isEmpty()) {
            throw new NotFoundUserByIdException("User not found with id = " + userID);
        }
    }
}
