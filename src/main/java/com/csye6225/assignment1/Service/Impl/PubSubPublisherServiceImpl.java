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
    @Async
    public void publishNewUserMessage(User user) {
        try {
            // Step 1: Call Vertex AI for risk assessment
            Map<String, Object> riskResult = riskScoringService.assessRisk(
                    user.getUsername(), user.getFirstName(), user.getLastName());

            double riskScore = (double) riskResult.getOrDefault("riskScore", 0.0);
            String riskReason = (String) riskResult.getOrDefault("riskReason", "unknown");

            // Step 2: Build enriched message
            String userJson = objectMapper.writeValueAsString(user);
            org.json.JSONObject enrichedMessage = new org.json.JSONObject(userJson);
            enrichedMessage.put("riskScore", riskScore);
            enrichedMessage.put("riskReason", riskReason);

            // Step 3: Publish enriched message
            String enrichedJson = enrichedMessage.toString();
            pubSubTemplate.publish(topicName, enrichedJson);
            logger.info("Published enriched message to topic: {} | riskScore: {} | reason: {}",
                    topicName, riskScore, riskReason);

        } catch (Exception e) {
            // Fallback: publish without AI enrichment so registration isn't blocked
            logger.error("AI enrichment failed, publishing unenriched message: {}", e.getMessage());
            try {
                String userJson = objectMapper.writeValueAsString(user);
                org.json.JSONObject fallbackMessage = new org.json.JSONObject(userJson);
                fallbackMessage.put("riskScore", 0.0);
                fallbackMessage.put("riskReason", "AI enrichment unavailable");
                pubSubTemplate.publish(topicName, fallbackMessage.toString());
            } catch (Exception ex) {
                logger.error("Failed to publish even unenriched message: {}", ex.getMessage());
            }
        }
    }
}
