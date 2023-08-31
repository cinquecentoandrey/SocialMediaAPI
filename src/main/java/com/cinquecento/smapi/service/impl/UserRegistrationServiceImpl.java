package com.cinquecento.smapi.service.impl;

import com.cinquecento.smapi.model.Status;
import com.cinquecento.smapi.model.User;
import com.cinquecento.smapi.repository.UserRepository;
import com.cinquecento.smapi.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRegistrationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
    }
}
