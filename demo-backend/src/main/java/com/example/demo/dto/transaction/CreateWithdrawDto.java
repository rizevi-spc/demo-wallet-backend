package com.example.demo.dto.transaction;

import com.example.demo.enumeration.OppositePartyType;
import com.example.demo.validation.annotation.ValidPartyId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * DTO for creating a withdraw transaction.
 *
 */
@ValidPartyId
public record CreateWithdrawDto(
        @NotNull(message = "{valid.null.error}") Long walletId,
        @Positive(message = "{valid.negative.zero.error}") BigDecimal amount,
        @NotNull(message = "{valid.null.error}") OppositePartyType destinationType,
        @NotNull(message = "{valid.null.error}") String destinationId
) implements HasParty {

    @Override
    public OppositePartyType partyType() {
        return destinationType;
    }

    @Override
    public String partyId() {
        return destinationId;
    }
}