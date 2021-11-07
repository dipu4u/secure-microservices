package io.microservices.auth.server.rest.model;

public class AuthenticationRequest {

    private String principal;
    private String password;
    private String twoFAToken;
    private String serviceName;
    private String clientId;

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTwoFAToken() {
        return twoFAToken;
    }

    public void setTwoFAToken(String twoFAToken) {
        this.twoFAToken = twoFAToken;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
