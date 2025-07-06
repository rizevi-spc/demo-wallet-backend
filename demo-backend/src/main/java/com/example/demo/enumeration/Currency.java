package com.example.demo.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Currency enumeration representing different currencies
 */
@RequiredArgsConstructor
@Getter
public enum Currency {
    TRY(20000), USD(1000), EUR(1000);

    private final Integer maxApprovableAmount;
}
