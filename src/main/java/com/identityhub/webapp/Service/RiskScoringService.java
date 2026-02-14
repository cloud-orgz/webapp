package com.identityhub.webapp.Service;

import java.util.Map;

public interface RiskScoringService {
    Map<String, Object> assessRisk(String email, String firstName, String lastName);
}