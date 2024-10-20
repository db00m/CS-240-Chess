package responses;

import requests.LoginRequest;

import java.util.UUID;

public record LoginResponse(String username, UUID authToken) {
}
