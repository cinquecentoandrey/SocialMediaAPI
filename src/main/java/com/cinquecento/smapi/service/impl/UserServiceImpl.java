package com.cinquecento.smapi.service.impl;

import com.cinquecento.smapi.model.User;
import com.cinquecento.smapi.repository.UserRepository;
import com.cinquecento.smapi.service.UserService;
import com.cinquecento.smapi.util.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
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
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username = " + username + " not found"));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id = " + id + " not found"));
    }

    @Override
    public List<User> findFriendsByUserId(Long id) {
        return userRepository.findFriendsByUserId(id);
    }

    @Override
    public List<User> findFollowersByUserId(Long id) {
        return userRepository.findFollowersByUserId(id);
    }

    @Override
    public List<User> findUnfriendedUsersByUserId(Long id) {
        return userRepository.findUnfriendedUsersByUserId(id);
    }

    @Override
    @Transactional
    public void subscribe(Long userId, Long subId) throws SQLException {
        userRepository.subscribe(userId, subId);
    }

    @Override
    @Transactional
    public void unsubscribe(Long userId, Long unsubId) {
        userRepository.unsubscribe(userId, unsubId);
    }

    @Override
    @Transactional
    public void addFriend(Long firstId, Long secondId) throws SQLException {
        userRepository.addFriend(firstId, secondId);
        userRepository.updateFriendship(firstId, secondId);
    }

    @Override
    @Transactional
    public void removeFriend(Long firstId, Long secondId) {
        userRepository.removeFriend(firstId, secondId);
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
    public boolean exist(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
