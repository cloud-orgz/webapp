package com.csye6225.assignment1.Service;

import java.util.Map;

public interface RiskScoringService {
    Map<String, Object> assessRisk(String email, String firstName, String lastName);
}