package com.cinquecento.smapi.service.impl;

import com.cinquecento.smapi.model.User;
import com.cinquecento.smapi.repository.UserRepository;
import com.cinquecento.smapi.service.UserService;
import com.cinquecento.smapi.util.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username = " + username + " not found"));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id = " + id + " not found"));
    }

    @Override
    @Transactional
    public void update(Long id, User userToUpdate) {
        userToUpdate.setLastUpdate(new Date());
        userRepository.partUpdate(id, userToUpdate.getUsername(),
                userToUpdate.getFirstName(), userToUpdate.getLastName(),
                userToUpdate.getAge(),
                userToUpdate.getTelephone(),
                userToUpdate.getEmail(),
                userToUpdate.getLastUpdate());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
