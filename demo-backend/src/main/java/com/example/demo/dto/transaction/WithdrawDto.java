package com.example.demo.dto.transaction;

import com.example.demo.enumeration.OppositePartyType;
import com.example.demo.enumeration.TransactionType;
import com.example.demo.validation.annotation.ValidPartyId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * DTO for withdraw transactions.
 * used for returning to client
 * */
@Data
@AllArgsConstructor
@ValidPartyId
@SuperBuilder
public class WithdrawDto extends TransactionDto {
    private OppositePartyType destinationType;

    private String destinationId;

    @Override
    public OppositePartyType partyType() {
        return destinationType;
    }

    @Override
    public String partyId() {
        return destinationId;
    }

    @Override
    public TransactionType getType() {
        return TransactionType.WITHDRAW;
    }
}