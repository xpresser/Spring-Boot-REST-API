package com.metodi.projectapp.services.impl;

import com.metodi.projectapp.controllers.dtos.LoginRequestDTO;
import com.metodi.projectapp.controllers.dtos.LoginResponseDTO;
import com.metodi.projectapp.controllers.dtos.UserRequestDTO;
import com.metodi.projectapp.controllers.dtos.UserResponseDTO;
import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.repositories.UserRepository;
import com.metodi.projectapp.services.TokenService;
import com.metodi.projectapp.services.UserService;
import com.metodi.projectapp.services.exceptions.NotFoundUserByIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final TokenService tokenService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           TokenService tokenService,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

        if (userRepository.count() == 0) {
            User user = initAdminUser();
            userRepository.save(user);
        }
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository
                .findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("No user with username and password"));

        if (!bCryptPasswordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new UsernameNotFoundException("No user with username and password");
        }

        return createLoginResponseDTO(user);
    }

    private LoginResponseDTO createLoginResponseDTO(User user) {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

        loginResponseDTO.setToken(
                tokenService.generateToken(user)
        );

        return loginResponseDTO;
    }

    @Override
    public List<UserResponseDTO> getAll() {
        return this.userRepository.findAll().stream().map(User::getResponseDTO)
                .sorted(Comparator.comparingLong(UserResponseDTO::getId)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public User createUser(UserRequestDTO userRequestDTO, User loggedUser) {
        String password = bCryptPasswordEncoder.encode(userRequestDTO.getPassword());
        userRequestDTO.setPassword(password);

        User user = new User();
        user.setUserRequestDtoParameters(userRequestDTO, loggedUser, user);

        return this.userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(long userID, UserResponseDTO userResponseDTO, User loggedUser) {
        User user = userRepository.findById(userID)
                .orElseThrow(()->new NotFoundUserByIdException("User not found with id = " + userID));

        user.setUserResponseDtoParameters(userResponseDTO, loggedUser, user);

        return this.userRepository.save(user);
    }

    @Override
    public void deleteById(long userID) {
        checkIfUserIdExists(userID);

        this.userRepository.deleteById(userID);
    }

    @Override
    public UserResponseDTO getById(long userID) {
        return this.userRepository.findById(userID).map(User::getResponseDTO)
                .orElseThrow(() -> new NotFoundUserByIdException("User not found with id = " + userID));
    }

    private void checkIfUserIdExists(long userID) {
        if (this.userRepository.findById(userID).isEmpty()) {
            throw new NotFoundUserByIdException("User not found with id = " + userID);
        }
    }

    private User initAdminUser() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword(bCryptPasswordEncoder.encode("password"));
        user.setFirstName("Administrator");
        user.setLastName("Administrator");
        user.setAdmin(true);

        return user;
    }
}
