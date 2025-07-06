package com.example.demo.dto.wallet;

import com.example.demo.enumeration.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * Create wallet request DTO
 */
public record CreateWalletDto(
    @NotBlank(message = "{valid.blank.error}") String name,
    @NotNull(message = "{valid.null.error}") Currency currency,
    boolean activeForShopping,
    boolean activeForWithdraw,
    @NotNull(message = "{valid.null.error}") Long customerId
) {
}