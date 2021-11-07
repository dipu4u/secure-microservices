package io.microservices.apigateway.rest.filter;

public class Config {

    private final String name;

    public Config(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
