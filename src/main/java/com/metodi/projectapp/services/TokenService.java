package com.metodi.projectapp.services;

import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.security.CustomUserPrincipal;

public interface TokenService {

    String generateToken(User user);

    CustomUserPrincipal parseToken(String token);
}
