package com.cinquecento.smapi.util;

import com.cinquecento.smapi.model.User;
import com.cinquecento.smapi.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class UserValidator implements Validator {

    private final UserServiceImpl userService;

    @Autowired
    public UserValidator(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        Optional<User> foundUSer = userService.findByUsername(user.getUsername());

        if (foundUSer.isPresent()) {
            errors.rejectValue("username", "", "This username is already taken.");
        }
    }
}
