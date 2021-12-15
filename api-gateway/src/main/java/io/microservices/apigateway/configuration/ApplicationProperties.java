package io.microservices.apigateway.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private TokenEncryption tokenEncryption;

    public TokenEncryption getTokenEncryption() {
        return tokenEncryption;
    }

    public void setTokenEncryption(TokenEncryption tokenEncryption) {
        this.tokenEncryption = tokenEncryption;
    }

    public static class TokenEncryption {
        private String scopeClaimName;
        private int expiryMinutes;
        private String signTokenKeyPath;

        public int getExpiryMinutes() {
            return expiryMinutes;
        }

        public void setExpiryMinutes(int expiryMinutes) {
            this.expiryMinutes = expiryMinutes;
        }

        public String getScopeClaimName() {
            return scopeClaimName;
        }

        public void setScopeClaimName(String scopeClaimName) {
            this.scopeClaimName = scopeClaimName;
        }

        public String getSignTokenKeyPath() {
            return signTokenKeyPath;
        }

        public void setSignTokenKeyPath(String signTokenKeyPath) {
            this.signTokenKeyPath = signTokenKeyPath;
        }
    }

}
