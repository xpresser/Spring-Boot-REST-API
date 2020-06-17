package com.metodi.projectapp.services;

import com.metodi.projectapp.controllers.dtos.LoginRequestDTO;
import com.metodi.projectapp.controllers.dtos.LoginResponseDTO;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.controllers.dtos.UserRequestDTO;
import com.metodi.projectapp.controllers.dtos.UserResponseDTO;

import java.util.List;

public interface UserService {

    List<UserResponseDTO> getAll();

    User createUser(UserRequestDTO userRequestDTO, User loggedUser);

    User updateUser(long userID, UserResponseDTO userResponseDTO, User loggedUser);

    void deleteById(long userID);

    UserResponseDTO getById(long userID);

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
