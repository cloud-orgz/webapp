package com.csye6225.assignment1.Service.Impl;

import com.csye6225.assignment1.entities.User;
import com.csye6225.assignment1.Service.UserService;
import com.csye6225.assignment1.dto.UserDto;
import com.csye6225.assignment1.dto.UserUpdateDto;
import com.csye6225.assignment1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserDto userDto) {
            if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
                throw new IllegalArgumentException("Email address already in use.");
            }

            User user = new User();
            user.setUsername(userDto.getUsername());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setAccountCreated(LocalDateTime.now());
            user.setAccount_updated(LocalDateTime.now());

            return userRepository.save(user);
        }

}
