package com.cinquecento.smapi.service;

import com.cinquecento.smapi.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);

    List<User> findAll();

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    void update(Long id, User updatedUser);

    void delete(Long id);
}
