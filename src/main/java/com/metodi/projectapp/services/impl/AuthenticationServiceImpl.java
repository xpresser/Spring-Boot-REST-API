package com.metodi.projectapp.services.impl;

import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.repositories.UserRepository;
import com.metodi.projectapp.security.CustomUserPrincipal;
import com.metodi.projectapp.services.AuthenticationService;
import com.metodi.projectapp.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getLoggedUser() {
        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User is not in the database"));
    }
}
