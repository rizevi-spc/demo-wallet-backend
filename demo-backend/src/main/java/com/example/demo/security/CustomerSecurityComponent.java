package com.example.demo.security;

import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Component for security checks related to customers authorization.
 * This component provides methods to check if a user is the owner of a customer or a wallet.
 */
@Component
@RequiredArgsConstructor
public class CustomerSecurityComponent {
    private final CustomerRepository customerRepository;
    private final WalletRepository walletRepository;

    @Cacheable(value = "customerOwner", key = " #customerId + '_' + #userId")
    public boolean isCustomerOwner(Long customerId, Long userId) {
        return customerRepository.existsByIdAndUserId(customerId, userId);
    }
    @Cacheable(value = "walletOwner", key = " #walletId + '_' + #userId")
    public boolean isCustomerWalletOwner(Long walletId, Long userId) {
        return walletRepository.existsByCustomer_IdOrCustomer_UserId(walletId, userId);
    }
}