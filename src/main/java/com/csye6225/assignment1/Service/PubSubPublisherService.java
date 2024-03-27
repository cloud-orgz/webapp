package com.csye6225.assignment1.Service;

import com.csye6225.assignment1.entities.User;
import org.springframework.stereotype.Service;

@Service
public interface PubSubPublisherService {

    public void publishNewUserMessage(User newUser);
}
