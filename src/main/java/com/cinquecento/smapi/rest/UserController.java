package com.cinquecento.smapi.rest;

import com.cinquecento.smapi.dto.PostDTO;
import com.cinquecento.smapi.dto.UserDTO;
import com.cinquecento.smapi.service.PostService;
import com.cinquecento.smapi.service.impl.UserServiceImpl;
import com.cinquecento.smapi.util.*;
import com.cinquecento.smapi.util.exception.InviteException;
import com.cinquecento.smapi.util.exception.UserNotFoundException;
import com.cinquecento.smapi.util.exception.UserNotUpdatedException;
import com.cinquecento.smapi.util.response.UserErrorResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserServiceImpl userService;
    private final UserConverter userConverter;
    private final ErrorMessageBuilder errorMessageBuilder;
    private final PostService postService;
    private final PostConverter postConverter;
    private final CurrentUserInfo currentUserInfo;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserServiceImpl userService,
                          UserConverter userConverter,
                          ErrorMessageBuilder errorMessageBuilder,
                          PostService postService,
                          PostConverter postConverter, CurrentUserInfo currentUserInfo, UserValidator userValidator) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.errorMessageBuilder = errorMessageBuilder;
        this.postService = postService;
        this.postConverter = postConverter;
        this.currentUserInfo = currentUserInfo;
        this.userValidator = userValidator;
    }

    @GetMapping("/my-page")
    public UserDTO get() {
        return userConverter.convertToUserDTO(currentUserInfo.getCurrentUser());
    }

    @GetMapping("/{id}")
    public UserDTO get(@PathVariable(name = "id") Long id) throws UserNotFoundException {
        return userConverter.convertToUserDTO(userService.findById(id));
    }

    @PatchMapping("/update")
    public UserDTO update(@RequestBody @Valid UserDTO userDTO,
                          BindingResult bindingResult) {

        userValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors())
            throw new UserNotUpdatedException(errorMessageBuilder.message(bindingResult));

        userService.update(currentUserInfo.getCurrentUser().getId(), userConverter.convertToUser(userDTO));

        return userDTO;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> delete() {
        userService.delete(currentUserInfo.getCurrentUser().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/posts")
    public List<PostDTO> posts(@PathVariable(name = "id") Long id) {
        return postService.findAllByUserId(id)
                .stream()
                .map(postConverter::convertToPostDTO)
                .toList();
    }

    @GetMapping("/{id}/friends")
    public List<UserDTO> friends(@PathVariable(name = "id") Long id) throws UserNotFoundException {
        return userService.findFriendsByUserId(id)
                .stream()
                .map(userConverter::convertToUserDTO)
                .toList();
    }

    @GetMapping("/{id}/subscribers")
    public List<UserDTO> subscribers(@PathVariable(name = "id") Long id) throws UserNotFoundException {
        return userService.findFollowersByUserId(id)
                .stream()
                .map(userConverter::convertToUserDTO)
                .toList();
    }

    @GetMapping("/current/friend-requests")
    public List<UserDTO> friendRequests() {
        return userService.findUnfriendedUsersByUserId(currentUserInfo.getCurrentUser().getId())
                .stream()
                .map(userConverter::convertToUserDTO)
                .toList();
    }

    @PostMapping("/{id}/send-invite")
    public ResponseEntity<?> sendInvite(@PathVariable(name = "id") Long id) throws UserNotFoundException {

        try {
            userService.subscribe(id, currentUserInfo.getCurrentUser().getId());
        } catch (SQLException e) {
            throw new InviteException(e.getMessage());
        }

        return new ResponseEntity<>("Send invite to user with id = " + id, HttpStatus.OK);
    }

    @PostMapping("/{id}/unsubscribe")
    public ResponseEntity<HttpStatus> unsubscribe(@PathVariable(name = "id") Long id) {

        if (userService.exist(id)) {
            userService.unsubscribe(id, currentUserInfo.getCurrentUser().getId());
        } else {
            throw new UserNotFoundException("User with id = " + id + " not found");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/delete-subscriber")
    public ResponseEntity<HttpStatus> deleteSubscriber(@PathVariable(name = "id") Long id) {

        if (userService.exist(id)) {
            userService.unsubscribe(currentUserInfo.getCurrentUser().getId(), id);
        } else {
            throw new UserNotFoundException("Subscriber with id = " + id + " not found");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/accept-invite")
    public ResponseEntity<HttpStatus> acceptInvite(@PathVariable(name = "id") Long id) {

        if (userService.exist(id)) {
            try {

                if (currentUserInfo.getCurrentUser().getId() < id) {
                    userService.addFriend(currentUserInfo.getCurrentUser().getId(), id);
                } else {
                    userService.addFriend(id, currentUserInfo.getCurrentUser().getId());
                }

                userService.subscribe(id, currentUserInfo.getCurrentUser().getId());
            } catch (SQLException e) {
                throw new InviteException("U can't send invite to this user");
            }
        } else {
            throw new UserNotFoundException("You don't have a subscriber with id = " + id);
        }

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

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handledException(InviteException exception) {
        UserErrorResponse response = new UserErrorResponse(
                exception.getMessage(),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
