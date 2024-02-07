package com.csye6225.assignment1.Service;

import com.csye6225.assignment1.entities.User;
import com.csye6225.assignment1.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public User createUser(UserDto userDto);

}
