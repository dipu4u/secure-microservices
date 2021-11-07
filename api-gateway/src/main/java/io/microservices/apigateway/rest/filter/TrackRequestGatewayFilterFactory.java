package io.microservices.apigateway.rest.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TrackRequestGatewayFilterFactory implements GatewayFilterFactory<Config> {

    private static final Logger LOG = LoggerFactory.getLogger(TrackRequestGatewayFilterFactory.class);

    private static final String CORRELATION_ID = "correlation-id";

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String correlationId = UUID.randomUUID().toString();
            LOG.info("Add Correlation id {}", correlationId);

            ServerHttpRequest request = exchange.getRequest().mutate().header(CORRELATION_ID, correlationId).build();
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().add(CORRELATION_ID, correlationId);

            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    @Override
    public Config newConfig() {
        return new Config("TrackRequest");
    }

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }
}
