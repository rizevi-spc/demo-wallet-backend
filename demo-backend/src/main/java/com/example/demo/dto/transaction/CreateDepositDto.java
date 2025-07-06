package com.example.demo.dto.transaction;

import com.example.demo.dto.transaction.HasParty;
import com.example.demo.enumeration.OppositePartyType;
import com.example.demo.validation.annotation.ValidPartyId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * DTO for creating a deposit transaction.
 *
 */
@ValidPartyId
public record CreateDepositDto(
        @NotNull(message = "{valid.null.error}") Long walletId,
        @Positive(message = "{valid.negative.zero.error}") BigDecimal amount,
        @NotNull(message = "{valid.null.error}") OppositePartyType sourceType,
        @NotNull(message = "{valid.null.error}") String sourceId
) implements HasParty {
    @Override
    public OppositePartyType partyType() {
        return sourceType;
    }

    @Override
    public String partyId() {
        return sourceId;
    }
}