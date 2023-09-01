package com.cinquecento.smapi.rest;

import com.cinquecento.smapi.dto.AuthenticationUserDTO;
import com.cinquecento.smapi.dto.UserDTO;
import com.cinquecento.smapi.model.User;
import com.cinquecento.smapi.security.JWTUtil;
import com.cinquecento.smapi.service.UserRegistrationService;
import com.cinquecento.smapi.util.ErrorMessageBuilder;
import com.cinquecento.smapi.util.UserConverter;
import com.cinquecento.smapi.util.UserValidator;
import com.cinquecento.smapi.util.exception.UserNotCreatedException;
import com.cinquecento.smapi.util.exception.UserNotLoginException;
import com.cinquecento.smapi.util.response.UserErrorResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRegistrationService userRegistrationService;
    private final UserValidator userValidator;
    private final JWTUtil jwtUtil;
    private final UserConverter userConverter;
    private final AuthenticationManager authenticationManager;
    private final ErrorMessageBuilder errorMessageBuilder;

    @Autowired
    public AuthController(UserRegistrationService userRegistrationService, UserValidator userValidator, JWTUtil jwtUtil, UserConverter userConverter, AuthenticationManager authenticationManager, ErrorMessageBuilder errorMessageBuilder) {
        this.userRegistrationService = userRegistrationService;
        this.userValidator = userValidator;
        this.jwtUtil = jwtUtil;
        this.userConverter = userConverter;
        this.authenticationManager = authenticationManager;
        this.errorMessageBuilder = errorMessageBuilder;
    }

    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody @Valid UserDTO userDTO,
                                                   BindingResult bindingResult) {
        User user = userConverter.convertToUser(userDTO);

        if (bindingResult.hasErrors())
            throw new UserNotCreatedException(errorMessageBuilder.message(bindingResult));

        userRegistrationService.register(user);

        String token = jwtUtil.generateToken(user.getUsername());
        return Map.of("jwt-token", token);
    }

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationUserDTO authenticationUserDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationUserDTO.getUsername(),
                        authenticationUserDTO.getPassword());

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new UserNotLoginException("Bad credentials", e);
        }

        String token = jwtUtil.generateToken(authenticationUserDTO.getUsername());
        return Map.of("jwt-token", token);
    }


    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handledException(UserNotCreatedException exception) {
        UserErrorResponse response = new UserErrorResponse(
                exception.getMessage(),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handledException(UserNotLoginException exception) {
        UserErrorResponse response = new UserErrorResponse(
                exception.getMessage(),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
