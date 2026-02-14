package com.identityhub.webapp.Service.Impl;

import com.identityhub.webapp.entities.User;
import com.identityhub.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomeUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Here you should build the UserDetails object however you need to.
        // For example, you might construct a Spring Security User like this:
        System.out.println(user.getPassword());
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // Make sure this password is encoded using the same PasswordEncoder bean
                .authorities("USER") // Assign authorities or roles as needed
                .build();
    }
}
