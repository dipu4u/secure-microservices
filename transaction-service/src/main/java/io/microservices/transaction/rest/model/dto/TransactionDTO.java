package io.microservices.transaction.rest.model.dto;

import java.math.BigDecimal;

public class TransactionDTO {

    private final String transactionId;
    private final BigDecimal amount;

    public TransactionDTO(String transactionId, BigDecimal amount) {
        this.transactionId = transactionId;
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
