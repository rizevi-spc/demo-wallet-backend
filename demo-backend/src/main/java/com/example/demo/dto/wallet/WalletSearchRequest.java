package com.example.demo.dto.wallet;

import com.example.demo.dto.PageRequestInfo;
import com.example.demo.enumeration.Currency;
import jakarta.validation.Valid;

import java.math.BigDecimal;

/**
 * Request DTO for searching wallets.
 * This record encapsulates the search criteria for wallets, including
 */
public record WalletSearchRequest(
        String name,
        Long customerId,
        Currency currency,
        Boolean activeForShopping,
        Boolean activeForWithdraw,
        BigDecimal balanceGreaterThan,
        BigDecimal balanceLessThan,
        @Valid PageRequestInfo pageRequestInfo
) {
}
