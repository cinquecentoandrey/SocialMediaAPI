package com.cinquecento.smapi.rest;

import com.cinquecento.smapi.dto.UserDTO;
import com.cinquecento.smapi.service.impl.UserServiceImpl;
import com.cinquecento.smapi.util.ErrorMessageBuilder;
import com.cinquecento.smapi.util.UserConverter;
import com.cinquecento.smapi.util.exception.UserNotFoundException;
import com.cinquecento.smapi.util.exception.UserNotUpdatedException;
import com.cinquecento.smapi.util.response.UserErrorResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserServiceImpl userService;
    private final UserConverter userConverter;
    private final ErrorMessageBuilder errorMessageBuilder;

    @Autowired
    public UserController(UserServiceImpl userService, UserConverter userConverter, ErrorMessageBuilder errorMessageBuilder) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.errorMessageBuilder = errorMessageBuilder;
    }

    @GetMapping("/{id}")
    public UserDTO get(@PathVariable(name = "id") Long id) throws UserNotFoundException {
        return userConverter.convertToUserDTO(userService.findById(id));
    }

    @PatchMapping("/{id}")
    public UserDTO update(@PathVariable(name = "id") Long id,
                          @RequestBody @Valid UserDTO userDTO,
                          BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new UserNotUpdatedException(errorMessageBuilder.message(bindingResult));

        userService.update(id, userService.findById(id));

        return userDTO;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handledException(UserNotFoundException exception) {
        UserErrorResponse response = new UserErrorResponse(
                exception.getMessage(),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handledException(UserNotUpdatedException exception) {
        UserErrorResponse response = new UserErrorResponse(
                exception.getMessage(),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_MODIFIED);
    }
}
