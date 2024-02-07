package com.csye6225.assignment1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserUpdateDto {

    @JsonProperty("first_name")
    @NotBlank(message = "First name must be passed and should not be empty")
    @Pattern(regexp = "^[A-Za-z ]*$", message = "First name must not contain special characters")
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank(message = "Last name must be passed and should not be empty")
    @Pattern(regexp = "^[A-Za-z ]*$", message = "Last name must not contain special characters")
    private String lastName;

    @NotBlank(message = "Password must be passed and should not be empty")
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
