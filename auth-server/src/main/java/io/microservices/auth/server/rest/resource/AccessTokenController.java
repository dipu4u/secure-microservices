package io.microservices.auth.server.rest.resource;

import io.microservices.auth.server.rest.model.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/token")
public class AccessTokenController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenController.class);

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessTokenResponse> getAccessToken() {
        LOGGER.info("Get Access Token request");
        return ResponseEntity.ok(new AccessTokenResponse(UUID.randomUUID().toString()));
    }

}
