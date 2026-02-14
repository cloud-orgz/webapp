package com.identityhub.webapp.Service.Impl;

import com.identityhub.webapp.Service.PubSubPublisherService;
import com.identityhub.webapp.entities.User;
import com.identityhub.webapp.Service.UserService;
import com.identityhub.webapp.dto.UserDto;
import com.identityhub.webapp.dto.UserUpdateDto;
import com.identityhub.webapp.entities.VerificationToken;
import com.identityhub.webapp.repository.UserRepository;
import com.identityhub.webapp.repository.VerificationTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PubSubPublisherService pubSubPublisherService;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
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
            logger.info("created user with username "+user.getUsername());
            User savedUser = userRepository.save(user);
            pubSubPublisherService.publishNewUserMessage(savedUser);
            return savedUser;
    }

    @Override
    public void updateUser(String username, UserUpdateDto updateDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (updateDto.getFirstName() != null && !updateDto.getFirstName().isBlank()) {
            user.setFirstName(updateDto.getFirstName());
        }

        if (updateDto.getLastName() != null && !updateDto.getLastName().isBlank()) {
            user.setLastName(updateDto.getLastName());
        }

        if (updateDto.getPassword() != null && !updateDto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        }

        userRepository.save(user);
    }

    @Override
    public User getUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return user;
    }

    @Override
    public boolean verifyUser(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);

        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false; // Token not found or expired
        }

        User user = verificationToken.getUser();
        if (user != null && !user.isVerified()) {
            user.setVerified(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean isUserVerified(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        System.out.println(user.isVerified());
        return user.isVerified();
    }

}
