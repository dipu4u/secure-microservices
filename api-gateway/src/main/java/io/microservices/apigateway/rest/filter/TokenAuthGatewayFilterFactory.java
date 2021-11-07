package io.microservices.apigateway.rest.filter;

import io.microservices.apigateway.exception.InvalidTokenException;
import io.microservices.apigateway.token.JwtTokenVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class TokenAuthGatewayFilterFactory implements GatewayFilterFactory<Config> {

    private static final Logger LOG = LoggerFactory.getLogger(TokenAuthGatewayFilterFactory.class);

    private final JwtTokenVerifier tokenVerifier;

    public TokenAuthGatewayFilterFactory(JwtTokenVerifier tokenVerifier) {
        this.tokenVerifier = tokenVerifier;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String endpoint = exchange.getRequest().getPath().value();
            LOG.info("Request for resource {}", endpoint);
            HttpHeaders headers = exchange.getRequest().getHeaders();
            headers.entrySet()
                    .forEach(entry -> LOG.debug("Header name={}, value={}", entry.getKey(), entry.getValue()));
            String token = getHeaderToken(headers);
            try {
                boolean valid = tokenVerifier.isValidToken(token);
                if (valid) {
                    return chain.filter(exchange);
                }
            } catch(InvalidTokenException e) {
                LOG.error(e.getMessage(), e);
            }
            LOG.error("Token Authentication error endpoint {}", endpoint);
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        };
    }

    @Override
    public Config newConfig() {
        return new Config("TokenAuth");
    }

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getHeaderToken(HttpHeaders headers) {
        return Optional.ofNullable(headers.get(HttpHeaders.AUTHORIZATION))
                .map(list -> list.get(0))
                .orElse(null);
    }
}
