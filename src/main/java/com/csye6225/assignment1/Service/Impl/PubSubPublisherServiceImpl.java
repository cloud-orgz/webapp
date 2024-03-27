package com.csye6225.assignment1.Service.Impl;

import com.csye6225.assignment1.Service.PubSubPublisherService;
import com.csye6225.assignment1.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PubSubPublisherServiceImpl implements PubSubPublisherService {

    @Autowired
    private PubSubTemplate pubSubTemplate;

    private final ObjectMapper objectMapper;

    @Value("${spring.cloud.gcp.pubsub.topic-name}")
    private String topicName;

    private static final Logger logger = LoggerFactory.getLogger(PubSubPublisherServiceImpl.class);

    public PubSubPublisherServiceImpl() {
        this.objectMapper = new ObjectMapper();
        // Register the JavaTimeModule to handle Java 8 Date and Time API types
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    @Override
    public void publishNewUserMessage(User user) {
        try {

            String userJson = objectMapper.writeValueAsString(user);
            pubSubTemplate.publish(topicName, userJson);
            logger.info("Sent message to topic: {}", topicName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
