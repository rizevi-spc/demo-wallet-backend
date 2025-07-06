package com.example.demo.dto.transaction;

import com.example.demo.enumeration.TransactionStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for updating the status of a transaction.
 * Contains the new transaction status.
 */
public record StatusUpdateRequest(
        @NotNull(message = "{valid.null.error}") TransactionStatus transactionStatus
) {
}