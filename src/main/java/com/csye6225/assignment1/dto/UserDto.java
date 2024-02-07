package com.csye6225.assignment1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.stereotype.Component;

@Component
public class UserDto {

    @JsonProperty("first_name")
    @NotBlank(message = "First name must be passed and should not be empty")
    @Pattern(regexp = "^[A-Za-z ]*$", message = "First name must not contain special characters")
    public String firstName;

    @JsonProperty("last_name")
    @NotBlank(message = "Last name must be passed and should not be empty")
    @Pattern(regexp = "^[A-Za-z ]*$", message = "Last name must not contain special characters")
    public String lastName;

    @NotBlank(message = "Password must be passed and should not be empty")
    public String password;

    @NotBlank(message = "Username must be passed and should be a valid email address")
    @Email(message = "Username must be a valid email address")
    public String username;

    public UserDto() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
