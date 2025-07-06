package com.example.demo.dto.transaction;

import com.example.demo.enumeration.TransactionStatus;
import com.example.demo.enumeration.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Abstract DTO for transactions.
 * Contains common fields for all transaction types.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class TransactionDto implements HasParty {
    private Long id;

    private Long walletId;

    private BigDecimal amount;
    
    private TransactionStatus status;

    private LocalDateTime createdAt;


    /**
     * Returns the type of the transaction.
     * @return transaction type
     */
    public abstract TransactionType getType();
}