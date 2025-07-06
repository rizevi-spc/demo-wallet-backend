package com.example.demo.dto.transaction;

import com.example.demo.enumeration.OppositePartyType;
import com.example.demo.enumeration.TransactionStatus;
import com.example.demo.enumeration.TransactionType;
import com.example.demo.validation.annotation.ValidPartyId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * DTO for deposit transactions.
 * used for returning to client
 * */
@Data
@AllArgsConstructor
@ValidPartyId
@SuperBuilder
public class DepositDto extends TransactionDto {
    private OppositePartyType sourceType;

    private String sourceId;

    @Override
    public OppositePartyType partyType() {
        return sourceType;
    }

    @Override
    public String partyId() {
        return sourceId;
    }

    @Override
    public TransactionType getType() {
        return TransactionType.DEPOSIT;
    }
}
