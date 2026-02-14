package com.identityhub.webapp.Service;

import com.identityhub.webapp.dto.UserUpdateDto;
import com.identityhub.webapp.entities.User;
import com.identityhub.webapp.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public User createUser(UserDto userDto);

    public void updateUser(String username, UserUpdateDto updateDto);

    public User getUser(String username);

    boolean verifyUser(String token);

    boolean isUserVerified(String username);

}
