package io.microservices.auth.server.rest.resource;

import io.microservices.auth.server.token.TokenProvider;
import io.microservices.auth.server.rest.model.AuthenticationResponse;
import io.microservices.auth.server.rest.model.AuthenticationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/oauth/authorize")
public class UserAuthenticationResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserAuthenticationResource.class);

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Autowired
    public UserAuthenticationResource(AuthenticationManager authenticationManager,
                                      TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> authorize(@RequestBody AuthenticationRequest request) {
        LOG.info("Authenticate request. Principal={}, Service={}, Client Id={}", request.getPrincipal(),
                request.getServiceName(), request.getClientId());
        Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getPrincipal(), request.getPassword()));
        if (authentication.isAuthenticated()) {
            User validUser = (User) authentication.getPrincipal();
            Set<String> roles = validUser.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());
            String jwtToken = tokenProvider.generateToken(validUser.getUsername(), roles);
            return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
        }
        LOG.error("Authenticate failed. Principal={}, Service={}, Client Id={}", request.getPrincipal(),
                request.getServiceName(), request.getClientId());
        throw new BadCredentialsException("Invalid credential");
    }

}
