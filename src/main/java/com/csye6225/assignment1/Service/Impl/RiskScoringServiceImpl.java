package com.csye6225.assignment1.Service.Impl;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativemodel.GenerativeModel;
import com.google.cloud.vertexai.generativemodel.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RiskScoringServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(RiskScoringService.class);

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;

    @Value("${vertex.ai.location:us-central1}")
    private String location;

    @Value("${vertex.ai.model:gemini-2.0-flash}")
    private String modelName;

    /**
     * Calls Vertex AI Gemini to assess registration risk.
     * Returns a Map with "riskScore" (0.0 to 1.0) and "riskReason".
     */
    public Map<String, Object> assessRisk(String email, String firstName, String lastName) {
        Map<String, Object> result = new HashMap<>();

        try (VertexAI vertexAI = new VertexAI(projectId, location)) {
            GenerativeModel model = new GenerativeModel(modelName, vertexAI);

            String prompt = buildPrompt(email, firstName, lastName);
            GenerateContentResponse response = model.generateContent(prompt);
            String responseText = ResponseHandler.getText(response);

            result = parseResponse(responseText);
            logger.info("Risk assessment for {}: score={}, reason={}",
                    email, result.get("riskScore"), result.get("riskReason"));

        } catch (Exception e) {
            logger.error("Vertex AI risk scoring failed for {}: {}", email, e.getMessage());
            // Default to low risk on failure — don't block registration
            result.put("riskScore", 0.0);
            result.put("riskReason", "Risk assessment unavailable — defaulting to low risk");
        }

        return result;
    }

    private String buildPrompt(String email, String firstName, String lastName) {
        String emailDomain = email.substring(email.indexOf("@") + 1);

        return String.format("""
                You are a registration risk assessment system. Analyze this new user registration and respond with ONLY a JSON object, no markdown, no explanation.

                Registration data:
                - Email: %s
                - Email domain: %s
                - First name: %s
                - Last name: %s

                Assess the risk based on:
                1. Is the email domain disposable/temporary (like mailinator, guerrillamail, tempmail, yopmail)?
                2. Does the name appear fake or auto-generated (random characters, keyboard patterns)?
                3. Does the email pattern look like a bot (random strings before @)?

                Respond with ONLY this JSON format:
                {"riskScore": 0.0, "riskReason": "brief reason"}

                riskScore: 0.0 = no risk, 1.0 = definite spam/bot.
                Keep riskReason under 20 words.
                """, email, emailDomain, firstName, lastName);
    }

    private Map<String, Object> parseResponse(String responseText) {
        Map<String, Object> result = new HashMap<>();

        try {
            // Clean up response — remove markdown code fences if present
            String cleaned = responseText.trim();
            if (cleaned.startsWith("```")) {
                cleaned = cleaned.replaceAll("```json\\s*", "").replaceAll("```\\s*", "");
            }

            // Simple JSON parsing
            org.json.JSONObject json = new org.json.JSONObject(cleaned);
            result.put("riskScore", json.getDouble("riskScore"));
            result.put("riskReason", json.getString("riskReason"));
        } catch (Exception e) {
            logger.warn("Failed to parse AI response: '{}'. Defaulting to low risk.", responseText);
            result.put("riskScore", 0.0);
            result.put("riskReason", "Could not parse risk assessment");
        }

        return result;
    }
}