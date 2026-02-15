package com.identityhub.webapp.controller;

import com.identityhub.webapp.entities.User;
import com.identityhub.webapp.Service.Impl.UserServiceImpl;
import com.identityhub.webapp.dto.UserDto;
import com.identityhub.webapp.dto.UserUpdateDto;
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
@RequestMapping("/v5/user")
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

    @PutMapping("/self")
    public ResponseEntity<?> updateUser(Principal principal,
                                       @Valid @RequestBody UserUpdateDto updateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Cast the Principal to UsernamePasswordAuthenticationToken to access UserDetails
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) principal;
        UserDetails userDetails = (UserDetails) authToken.getPrincipal();

        boolean isVerified = service.isUserVerified(userDetails.getUsername());

        if(!isVerified){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User Not Verified");
        }
        // Use the username from UserDetails to update the user
        service.updateUser(userDetails.getUsername(), updateDto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/self")
    public ResponseEntity<?> getUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Cast the Principal to UsernamePasswordAuthenticationToken to access UserDetails
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) principal;
        UserDetails userDetails = (UserDetails) authToken.getPrincipal();

        User user = service.getUser(userDetails.getUsername());
        Map<String, Object> responseBody = UserRequestBody(user);

        boolean isVerified = service.isUserVerified(userDetails.getUsername());

        if(!isVerified){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User Not Verified");
        }

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) principal;
        UserDetails userDetails = (UserDetails) authToken.getPrincipal();

        try {
            service.resendVerification(userDetails.getUsername());
            return ResponseEntity.ok("Verification email resent successfully.");
        } catch (IllegalStateException e) {
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
