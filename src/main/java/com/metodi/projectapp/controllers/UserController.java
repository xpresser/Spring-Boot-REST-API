package com.metodi.projectapp.controllers;

import com.metodi.projectapp.controllers.dtos.LoginRequestDTO;
import com.metodi.projectapp.controllers.dtos.LoginResponseDTO;
import com.metodi.projectapp.controllers.dtos.UserRequestDTO;
import com.metodi.projectapp.controllers.dtos.UserResponseDTO;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.services.AuthenticationService;
import com.metodi.projectapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final AuthenticationService authenticationService;

    @Autowired
    public UserController(UserService userService,
                          AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return userService.login(loginRequestDTO);
    }

    @GetMapping
    public List<UserResponseDTO> getAll() {
        return this.userService.getAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User create(@RequestBody UserRequestDTO userRequestDTO) {

        User loggedUser = authenticationService.getLoggedUser();

        return this.userService.createUser(userRequestDTO, loggedUser);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User update(@PathVariable(value = "id") long userID,
                       @RequestBody UserResponseDTO userResponseDTO) {

        User loggedUser = authenticationService.getLoggedUser();

        return this.userService.updateUser(userID, userResponseDTO, loggedUser);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(@PathVariable(value = "id") long userID) {

        this.userService.deleteById(userID);
    }

    @GetMapping("/{id}")
    public UserResponseDTO getById(@PathVariable(value = "id") long userID) {
        return this.userService.getById(userID);
    }
}
