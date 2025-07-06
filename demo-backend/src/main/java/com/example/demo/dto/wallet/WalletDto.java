package com.example.demo.dto.wallet;

import com.example.demo.enumeration.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for Wallet.
 */
public record WalletDto(
    @NotNull(message = "{valid.null.error}")
    Long id,
    @NotBlank(message = "{valid.blank.error}") String name,
    @NotNull(message = "{valid.null.error}") Currency currency,
    boolean activeForShopping,
    boolean activeForWithdraw,
    BigDecimal balance,
    @NotNull(message = "{valid.null.error}") Long customerId
) {
}