package com.identityhub.webapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserUpdateDto {

    @JsonProperty("first_name")
    @Size(min=1, message="first_name cannot be empty if passed")
    @Pattern(regexp = "^[A-Za-z ]*$", message = "First name must not contain special characters")
    private String firstName;

    @JsonProperty("last_name")
    @Size(min=1, message="last_name cannot be empty if passed")
    @Pattern(regexp = "^[A-Za-z ]*$", message = "Last name must not contain special characters")
    private String lastName;

    @Size(min=1, message="password cannot be empty if passed")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "Password must be at least 8 characters long and include letters and numbers")
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
