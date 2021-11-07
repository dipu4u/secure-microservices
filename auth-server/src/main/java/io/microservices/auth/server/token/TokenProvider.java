package io.microservices.auth.server.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.microservices.auth.server.configuration.ApplicationProperties;
import io.microservices.auth.server.utils.EncryptionUtils;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private final ApplicationProperties applicationProperties;
    private final PrivateKey signTokenKey;

    public TokenProvider(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        signTokenKey = getSignTokenKey();
    }

    public String generateToken(String user, Set<String> permissions) {
        final Date issueDate = new Date(System.currentTimeMillis());
        String permissionsClaim = permissions.stream().collect(Collectors.joining(","));

        return Jwts.builder()
                .claim(applicationProperties.getTokenEncryption().getScopeClaimName(), permissionsClaim)
                .setSubject(user)
                .setIssuedAt(issueDate)
                .setExpiration(new Date(issueDate.getTime() +
                        (applicationProperties.getTokenEncryption().getExpiryMinutes() * 60 * 1000)))
                .signWith(SignatureAlgorithm.RS256, signTokenKey)
                .compact();
    }

    private PrivateKey getSignTokenKey() {
        try {
            return EncryptionUtils.readPrivateKey(applicationProperties.getTokenEncryption().getSignTokenKeyPath());
        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
            throw new ApplicationContextException("Token encryption load error. " + e.getMessage(), e);
        }
    }
}
