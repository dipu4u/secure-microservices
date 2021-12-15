package io.microservices.transaction.rest.resource;

import io.microservices.transaction.rest.model.dto.TransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionResource {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionResource.class);

    @PreAuthorize("hasAuthority('transaction:read')")
    @GetMapping(value = "/{accountNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDTO> getTransactions(@PathVariable("accountNo") String accountNo) {
        LOG.info("Get transactions for account {}", accountNo);
        return ResponseEntity.ok(new TransactionDTO(UUID.randomUUID().toString(), BigDecimal.valueOf(10.01)));
    }

}
