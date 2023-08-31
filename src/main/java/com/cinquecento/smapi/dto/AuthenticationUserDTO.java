package com.cinquecento.smapi.dto;

import jakarta.persistence.Column;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class AuthenticationUserDTO {

    @NotEmpty(message = "Username should not be empty")
    @Size(min = 2, max = 16, message = "Username length should be between 2 and 16 symbols")
    private String username;

    @NotEmpty(message = "Password should not be empty")
    private String password;

    public String getUsername() { return username; }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
