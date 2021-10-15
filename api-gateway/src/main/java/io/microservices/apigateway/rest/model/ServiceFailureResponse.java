package io.microservices.apigateway.rest.model;

public class ServiceFailureResponse {

    private final Integer status;
    private final String message;

    public ServiceFailureResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
