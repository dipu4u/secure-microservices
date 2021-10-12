package io.microservices.auth.server.rest.model;

public class AccessTokenResponse {

    private final String accessToken;

    public AccessTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
