package io.microservices.apigateway.token;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import io.microservices.apigateway.exception.InvalidTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

@Service
public class JwtTokenVerifier {

    private static final Logger LOG = LoggerFactory.getLogger(JwtTokenVerifier.class);

    public boolean isValidToken(String token) {
        LOG.info("Validate token {}", token);
        if (Objects.isNull(token) || token.trim().isEmpty()) {
            return false;
        }
        try {
            JWT jwt = JWTParser.parse(token);
            Date expiry = jwt.getJWTClaimsSet().getExpirationTime();
            return Objects.nonNull(expiry) || expiry.after(new Date());
        } catch (ParseException e) {
            throw new InvalidTokenException(e.getMessage(), e);
        }
    }

}
