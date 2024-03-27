package com.csye6225.assignment1.IntegrationTests;

import com.csye6225.assignment1.entities.User;
import com.csye6225.assignment1.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private String generateRandomEmail() {
        return "testuser+" + UUID.randomUUID().toString() + "@example.com";
    }

    private String createBasicAuthToken(String username, String password) {
        String auth = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
    }

    @Test
    public void testCreateUser() {
        String url = "/v1/user";
        String getUrl = "/v1/user/self";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String generatedEmail = generateRandomEmail();
        String password = "1234";
        String authToken = createBasicAuthToken(generatedEmail, password);

        Map<String, Object> user = new HashMap<>();
        user.put("first_name", "Jane");
        user.put("last_name", "Doe");
        user.put("password", password);
        user.put("username", generatedEmail);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        User u = userRepository.findByUsername(generatedEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + generatedEmail));
        u.setVerified(true);
        userRepository.save(u);
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.add("Authorization", authToken);
        HttpEntity<String> getRequest = new HttpEntity<>(getHeaders);

        ResponseEntity<String> getResponse = restTemplate.exchange(getUrl, HttpMethod.GET, getRequest, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        JSONObject jsonResponse = new JSONObject(getResponse.getBody());
        String returnedUsername = jsonResponse.getString("username");
        assertThat(returnedUsername).isEqualTo(generatedEmail);
    }

    @Test
    public void testUpdateUserAndValidate() {
        String createUserUrl = "/v1/user";
        String getUserUrl = "/v1/user/self";
        String updateUserUrl = "/v1/user/self";

        String generatedEmail = generateRandomEmail();
        String password = "1234";
        String authToken = createBasicAuthToken(generatedEmail, password);

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

        User u = userRepository.findByUsername(generatedEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + generatedEmail));
        u.setVerified(true);
        userRepository.save(u);

        HttpHeaders updateHeaders = new HttpHeaders();
        updateHeaders.setContentType(MediaType.APPLICATION_JSON);
        updateHeaders.add("Authorization", authToken);
        Map<String, Object> updatedUserDetails = new HashMap<>();
        updatedUserDetails.put("first_name", "UpdatedJane");
        updatedUserDetails.put("last_name", "UpdatedDoe");
        HttpEntity<Map<String, Object>> updateRequest = new HttpEntity<>(updatedUserDetails, updateHeaders);

        ResponseEntity<String> updateResponse = restTemplate.exchange(updateUserUrl, HttpMethod.PUT, updateRequest, String.class);
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.add("Authorization", authToken);
        HttpEntity<String> getRequest = new HttpEntity<>(getHeaders);

        ResponseEntity<String> getResponse = restTemplate.exchange(getUserUrl, HttpMethod.GET, getRequest, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        JSONObject jsonResponse = new JSONObject(getResponse.getBody());
        String updatedFirstName = jsonResponse.getString("first_name");
        assertThat(updatedFirstName).isEqualTo("UpdatedJane");
        String updatedLastName = jsonResponse.getString("last_name");
        assertThat(updatedLastName).isEqualTo("UpdatedDoe");
    }
}
