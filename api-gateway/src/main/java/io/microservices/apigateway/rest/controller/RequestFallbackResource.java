package io.microservices.apigateway.rest.controller;

import io.microservices.apigateway.rest.model.ServiceFailureResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/fallback")
@RestController
public class RequestFallbackResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestFallbackResource.class);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceFailureResponse> handleFallback() {
        LOGGER.info("Request failure fallback");
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new ServiceFailureResponse(HttpStatus.BAD_GATEWAY.value(), "Target service unavailable"));
    }

}
