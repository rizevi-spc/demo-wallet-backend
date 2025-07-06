package com.example.demo.service.impl;

import com.example.demo.aspect.annotation.Logging;
import com.example.demo.dto.wallet.CreateWalletDto;
import com.example.demo.dto.wallet.WalletDto;
import com.example.demo.dto.wallet.WalletSearchRequest;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Wallet;
import com.example.demo.exception.CustomValidationException;
import com.example.demo.mapper.WalletMapper;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.WalletRepository;
import com.example.demo.security.CustomerSecurityComponent;
import com.example.demo.service.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of the {@link WalletService} for managing wallet-related operations.
 */
@Transactional
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final CustomerRepository customerRepository;
    private final WalletMapper walletMapper;
    private final CustomerSecurityComponent customerSecurityComponent;

    @Override
    @Logging
    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CUSTOMER') and" +
            " @customerSecurityComponent.isCustomerOwner(#createWalletDto.customerId, authentication.principal.claims['user_id']))")
    public WalletDto add(CreateWalletDto createWalletDto) {

        final Customer customer = Optional.ofNullable(createWalletDto)
                .map(CreateWalletDto::customerId)
                .flatMap(customerRepository::findById)
                .filter(Objects::nonNull)
                .orElseThrow(() ->
                        new CustomValidationException("valid.customer.not.exist", createWalletDto,
                                CreateWalletDto::customerId));
        final Wallet wallet = walletMapper.toEntity(createWalletDto);
        wallet.setCustomer(customer);
        wallet.setBalance(BigDecimal.ZERO);
        return walletMapper.toDTO(walletRepository.save(wallet));
    }
    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CUSTOMER') and" +
            " @customerSecurityComponent.isCustomerOwner(#searchRequest.customerId, authentication.principal.claims['user_id']))")
    @Override
    @Logging
    public Page<WalletDto> getWallets(WalletSearchRequest searchRequest) {
        return walletRepository.searchWallets(searchRequest)
                .map(walletMapper::toDTO);
    }


}
