package com.cinquecento.smapi.service;

import com.cinquecento.smapi.model.User;

import java.util.List;

public interface UserService {

    void save(User user);

    List<User> findAll();

    User findByUsername(String username);

    User findById(Long id);

    void update(Long id, User updatedUser);

    void delete(Long id);
}
