package com.metodi.projectapp.repositories;

import com.metodi.projectapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Custom query method
     * For finding a User in database by Username and Password
     */
    Optional<User> findByUsername(String username);
}
