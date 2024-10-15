package responses;

import requests.LoginRequest;

import java.util.UUID;

public record LoginResponse(String username, UUID authToken, String message) {
    public LoginResponse(String username, UUID authToken) {
        this(username, authToken, null);
    }

    public LoginResponse(String message) {
        this(null, null, message);
    }
}
