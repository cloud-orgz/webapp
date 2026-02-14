package com.identityhub.webapp.Service;

import com.identityhub.webapp.entities.User;
import org.springframework.stereotype.Service;

@Service
public interface PubSubPublisherService {

    public void publishNewUserMessage(User newUser);
}
