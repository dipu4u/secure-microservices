package io.microservices.apigateway.rest.filter;

import io.microservices.apigateway.token.JwtTokenVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

@Component
public class TokenAuthGatewayFilterFactory implements GatewayFilterFactory<Config> {

    private static final String TOKEN_PREFIX = "Bearer";
    private static final Logger LOG = LoggerFactory.getLogger(TokenAuthGatewayFilterFactory.class);

    private final JwtTokenVerifier tokenVerifier;
    private final AuthErrorFilter authErrorFilter;

    public TokenAuthGatewayFilterFactory(JwtTokenVerifier tokenVerifier,
                                         ErrorWebExceptionHandler errorWebExceptionHandler) {
        this.tokenVerifier = tokenVerifier;
        this.authErrorFilter = new AuthErrorFilter(errorWebExceptionHandler);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String endpoint = exchange.getRequest().getPath().value();
            LOG.info("Request for resource {}", endpoint);
            HttpHeaders headers = exchange.getRequest().getHeaders();
            headers.entrySet().forEach(entry -> LOG.debug("Header name={}, value={}", entry.getKey(), entry.getValue()));
            String token = getHeaderToken(headers);
            if (Objects.nonNull(token) && tokenVerifier.isValidToken(token)) {
                return chain.filter(exchange);
            }
            LOG.error("Token Authentication error endpoint {}", endpoint);
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return authErrorFilter.filter(exchange, chain);
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

    private Mono<Void> onError(ServerWebExchange exchange) {
        LOG.info("Authentication error occurred.");
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.setRawStatusCode(401);
        return response.setComplete();
    }

    private String getHeaderToken(HttpHeaders headers) {
        return Optional.ofNullable(headers.get(HttpHeaders.AUTHORIZATION))
                .map(list -> list.get(0))
                .filter(token -> token.startsWith(TOKEN_PREFIX))
                .map(token -> token.substring(6).trim())
                .orElse(null);
    }
}
