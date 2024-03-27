package com.csye6225.assignment1.Service;

import com.csye6225.assignment1.dto.UserUpdateDto;
import com.csye6225.assignment1.entities.User;
import com.csye6225.assignment1.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public User createUser(UserDto userDto);

    public void updateUser(String username, UserUpdateDto updateDto);

    public User getUser(String username);

    boolean verifyUser(String token);

    boolean isUserVerified(String username);

}
