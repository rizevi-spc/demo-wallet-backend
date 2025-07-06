package com.example.demo.service.impl;

import com.example.demo.aspect.annotation.Logging;
import com.example.demo.dto.transaction.CreateDepositDto;
import com.example.demo.dto.transaction.DepositDto;
import com.example.demo.entity.DepositTransaction;
import com.example.demo.entity.Wallet;
import com.example.demo.enumeration.TransactionStatus;
import com.example.demo.exception.CustomValidationException;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.WalletRepository;
import com.example.demo.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Implementation of the DepositService interface for handling deposit transactions.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DepositServiceImpl implements DepositService {
    private final WalletRepository walletRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

    @Override
    @Logging
    @PreAuthorize("hasRole('EMPLOYEE') or (hasRole('CUSTOMER') and" +
            " @customerSecurityComponent.isCustomerWalletOwner(#createDepositDto.walletId(), authentication.principal.claims['user_id']))")
    public DepositDto processDeposit(CreateDepositDto createDepositDto) {
        Wallet wallet = walletRepository.findById(createDepositDto.walletId())
                .orElseThrow(() -> new CustomValidationException("valid.wallet.not.exist", createDepositDto.walletId()));

        DepositTransaction depositTransaction = transactionMapper.createDepositDtoToEntity(createDepositDto);
        depositTransaction.setWallet(wallet);
        final TransactionStatus status = getStatus(depositTransaction, wallet);
        depositTransaction.setStatus(status);

        if (TransactionStatus.APPROVED.equals(status)) {
            wallet.deposit(depositTransaction.getAmount());
        }

        return transactionMapper.depositToDto(transactionRepository.save(depositTransaction));
    }

    private TransactionStatus getStatus(DepositTransaction depositTransaction, Wallet wallet) {
        return depositTransaction.getAmount().compareTo(BigDecimal.valueOf(wallet.getCurrency().getMaxApprovableAmount())) > 0
                ? TransactionStatus.PENDING : TransactionStatus.APPROVED;
    }
}