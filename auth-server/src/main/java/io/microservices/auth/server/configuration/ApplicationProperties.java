package io.microservices.auth.server.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private UserEncryption userEncryption;
    private TokenEncryption tokenEncryption;

    public UserEncryption getUserEncryption() {
        return userEncryption;
    }

    public void setUserEncryption(UserEncryption userEncryption) {
        this.userEncryption = userEncryption;
    }

    public TokenEncryption getTokenEncryption() {
        return tokenEncryption;
    }

    public void setTokenEncryption(TokenEncryption tokenEncryption) {
        this.tokenEncryption = tokenEncryption;
    }

    public static class UserEncryption {
        private String passwordSalt;
        private int iterations;
        private int hashWidth;

        public String getPasswordSalt() {
            return passwordSalt;
        }

        public void setPasswordSalt(String passwordSalt) {
            this.passwordSalt = passwordSalt;
        }

        public int getIterations() {
            return iterations;
        }

        public void setIterations(int iterations) {
            this.iterations = iterations;
        }

        public int getHashWidth() {
            return hashWidth;
        }

        public void setHashWidth(int hashWidth) {
            this.hashWidth = hashWidth;
        }
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
