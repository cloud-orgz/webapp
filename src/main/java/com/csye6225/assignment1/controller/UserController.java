package com.csye6225.assignment1.controller;

import com.csye6225.assignment1.entities.User;
import com.csye6225.assignment1.Service.Impl.UserServiceImpl;
import com.csye6225.assignment1.dto.UserDto;
import com.csye6225.assignment1.dto.UserUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/v1/user")
public class UserController {


    @Autowired
    UserServiceImpl service;

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }
        try {
            User user = service.createUser(userDto);
            Map<String, Object> responseBody = UserRequestBody(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
        }
            catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    private Map<String, Object> UserRequestBody(User user) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("id", user.getId().toString());
        responseBody.put("first_name", user.getFirstName());
        responseBody.put("last_name", user.getLastName());
        responseBody.put("username", user.getUsername());
        responseBody.put("account_created", user.getAccountCreated().toString());
        responseBody.put("account_updated", user.getAccount_updated().toString());
        return responseBody;
    }


}
