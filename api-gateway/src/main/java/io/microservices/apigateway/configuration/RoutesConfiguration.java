package io.microservices.apigateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutesConfiguration {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/token/**")
                        .filters(f -> f.rewritePath("/api/token/(?<parts>.*)", "/${parts}")
                                .addRequestHeader("X-Forwarded-By", "api-gateway"))
                        .uri("lb://api-gateway"))
                .build();
    }

}
