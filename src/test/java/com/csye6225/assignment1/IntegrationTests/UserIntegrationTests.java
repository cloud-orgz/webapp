package com.csye6225.assignment1.IntegrationTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    String generatedEmail = generateRandomEmail();

    String password = "1234";

    String authToken = createBasicAuthToken(generatedEmail, password);

    @Test
    public void testCreateUser() {
        String url = "/v1/user";
        String getUrl = "/v1/user/self";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> user = new HashMap<>();
        user.put("first_name", "Jane");
        user.put("last_name", "Doe");
        user.put("password", password);
        user.put("username", generatedEmail);

        // Construct the request entity
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);

        // Send POST request
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        // Log the request and response for debugging
        System.out.println("Request Body: " + request.getBody());
        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());

        // Assert the response status code
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.add("Authorization", authToken);
        HttpEntity<String> getRequest = new HttpEntity<>(getHeaders);

        ResponseEntity<String> getResponse = restTemplate.exchange(getUrl, HttpMethod.GET, getRequest, String.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Log for debugging
        System.out.println("Get Response Body: " + getResponse.getBody());

        assertThat(getResponse.getBody()).contains(generatedEmail);
    }

    @Test
    public void testUpdateUserAndValidate() {
        // Create a new user
        String createUserUrl = "/v1/user";
        String getUserUrl = "/v1/user/self";
        String updateUserUrl = "/v1/user/self";
        String generatedEmail = generateRandomEmail();
        String password = "1234";

        HttpHeaders createHeaders = new HttpHeaders();
        createHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("first_name", "Jane");
        newUser.put("last_name", "Doe");
        newUser.put("password", password);
        newUser.put("username", generatedEmail);
        HttpEntity<Map<String, Object>> createRequest = new HttpEntity<>(newUser, createHeaders);

        ResponseEntity<String> createResponse = restTemplate.postForEntity(createUserUrl, createRequest, String.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Update the user
        HttpHeaders updateHeaders = new HttpHeaders();
        updateHeaders.setContentType(MediaType.APPLICATION_JSON);
        String authToken = createBasicAuthToken(generatedEmail, password);
        updateHeaders.add("Authorization", authToken);
        Map<String, Object> updatedUserDetails = new HashMap<>();
        updatedUserDetails.put("first_name", "UpdatedJane");
        updatedUserDetails.put("last_name", "UpdatedDoe");
        HttpEntity<Map<String, Object>> updateRequest = new HttpEntity<>(updatedUserDetails, updateHeaders);

        ResponseEntity<String> updateResponse = restTemplate.exchange(updateUserUrl, HttpMethod.PUT, updateRequest, String.class);
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Validate the update
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.add("Authorization", authToken);
        HttpEntity<String> getRequest = new HttpEntity<>(getHeaders);

        ResponseEntity<String> getResponse = restTemplate.exchange(getUserUrl, HttpMethod.GET, getRequest, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println("Updated Get Response Body: " + getResponse.getBody());

        // Verify the updated details are present in the response
        assertThat(getResponse.getBody()).contains("UpdatedJane");
        assertThat(getResponse.getBody()).contains("UpdatedDoe");
    }
    private String generateRandomEmail() {
        return "testuser+" + UUID.randomUUID().toString() + "@example.com";
    }

    private String createBasicAuthToken(String username, String password) {
        String auth = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
    }
}