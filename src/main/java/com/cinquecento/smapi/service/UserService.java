package com.cinquecento.smapi.service;

import com.cinquecento.smapi.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.SQLException;
import java.util.List;

public interface UserService {

    void save(User user);

    List<User> findAll();

    User findByUsername(String username);

    User findById(Long id);

    List<User> findFriendsByUserId(Long id);

    List<User> findFollowersByUserId(Long id);

    void subscribe(Long userId, Long subId) throws SQLException;

    void unsubscribe(Long userId, Long unsubId);

    void addFriend(Long firstId, Long secondId) throws SQLException;

    void removeFriend(Long firstId, Long secondId);

    void update(Long id, User userToUpdate);

    void delete(Long id);
}
