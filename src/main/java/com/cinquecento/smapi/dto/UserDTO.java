package com.cinquecento.smapi.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Date;


public class UserDTO {

    private Long id;

    @NotEmpty(message = "Username should not be empty")
    @Size(min = 2, max = 16, message = "Username length should be between 2 and 16 symbols")
    private String username;

    @NotEmpty(message = "First name should not be empty")
    @Size(min = 2, max = 30, message = "First name length should be between 2 and 30 symbols")
    private String firstName;

    @NotEmpty(message = "Last name should not be empty")
    @Size(min = 2, max = 30, message = "Last name length should be between 2 and 30 symbols")
    private String lastName;

    @Min(value = 0, message = "Age should be greater than zero")
    @Max(value = 120, message = "Age should be less than 120")
    private Integer age;

    @Email
    @NotEmpty(message = "Email should not not be empty")
    private String email;

    @NotEmpty(message = "Field should not be empty")
    @Pattern(regexp = "^(\\+7|7|8)?[\\s\\-]?\\(?[489][0-9]{2}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$",
            message = "Correct format: +79855310868 | +7 (926) 777-77-77 | 89855310868")
    private String telephone;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
