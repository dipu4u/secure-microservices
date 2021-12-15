package io.microservices.transaction.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.microservices.transaction.configuration.ApplicationProperties;
import io.microservices.transaction.utils.EncryptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenVerifier {

    private static final Logger LOG = LoggerFactory.getLogger(JwtTokenVerifier.class);

    private final ApplicationProperties applicationProperties;
    private final PublicKey tokenSignKey;

    public JwtTokenVerifier(ApplicationProperties applicationProperties) throws NoSuchAlgorithmException,
            IOException, InvalidKeySpecException {
        this.applicationProperties = applicationProperties;
        this.tokenSignKey = EncryptionUtils.readPublicKey(applicationProperties.getTokenEncryption().getSignTokenKeyPath());
    }

    public Authentication getAuthentication(String token) {
        try {
            Jws<Claims> jws = parse(token);
            if (jws.getBody().getExpiration().after(new Date())) {
                return new UsernamePasswordAuthenticationToken(jws.getBody().getSubject(), null, getAuthorities(jws));
            }
        } catch(ExpiredJwtException e) {
            LOG.error("Expired token {}", e.getMessage(), e);
        } catch(UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            LOG.error("Invalid token {}", e.getMessage(), e);
        } catch(Exception e) {
            LOG.error("Unexpected error during parse token {}", e.getMessage(), e);
        }
        return null;
    }

    private List<GrantedAuthority> getAuthorities(Jws<Claims> jws) {
        String authority = jws.getBody().get(applicationProperties.getTokenEncryption().getScopeClaimName(), String.class);
        return Arrays.stream(authority.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
    }

}
